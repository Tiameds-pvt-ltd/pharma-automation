package com.pharma.service.impl;

import com.pharma.dto.ItemDto;
import com.pharma.entity.DoctorEntity;
import com.pharma.entity.ItemEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.ItemMapper;

import com.pharma.repository.ItemRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.ItemService;

import com.pharma.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(itemDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        ItemEntity itemEntity = itemMapper.mapToItemEntity(itemDto);
        itemEntity.setItemId(UUID.randomUUID());
        itemEntity.setCreatedBy(user.getId());
        itemEntity.setCreatedDate(LocalDate.now());

        itemEntity.setPharmacyId(itemDto.getPharmacyId());

        ItemEntity savedItem = itemRepository.save(itemEntity);
        return itemMapper.mapToItemDto(savedItem);
    }

    @Transactional
    @Override
    public List<ItemDto> getAllItem(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<ItemEntity> items = itemRepository.findAllByPharmacyId(pharmacyId);
        return items.stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemDto getItemById(Long pharmacyId, UUID itemId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<ItemEntity> itemEntity = itemRepository.findByItemIdAndPharmacyId(itemId, pharmacyId);

        if (itemEntity.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with ID: " + itemId + " for pharmacy ID: " + pharmacyId);
        }
        return itemMapper.mapToItemDto(itemEntity.get());
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long pharmacyId, UUID itemId, ItemDto updatedItem, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<ItemEntity> itemEntityOptional = itemRepository.findByItemIdAndPharmacyId(itemId, pharmacyId);

        if (itemEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with ID: " + itemId +
                    " for pharmacy ID: " + pharmacyId);
        }

        ItemEntity itemEntity = itemEntityOptional.get();

        itemEntity.setItemName(updatedItem.getItemName());
        itemEntity.setPurchaseUnit(updatedItem.getPurchaseUnit());
        itemEntity.setVariantName(updatedItem.getVariantName());
        itemEntity.setUnitName(updatedItem.getUnitName());
        itemEntity.setManufacturer(updatedItem.getManufacturer());
        itemEntity.setPurchasePrice(updatedItem.getPurchasePrice());
        itemEntity.setMrpSalePrice(updatedItem.getMrpSalePrice());
        itemEntity.setPurchasePricePerUnit(updatedItem.getPurchasePricePerUnit());
        itemEntity.setMrpSalePricePerUnit(updatedItem.getMrpSalePricePerUnit());
        itemEntity.setGstPercentage(updatedItem.getGstPercentage());
        itemEntity.setGenericName(updatedItem.getGenericName());
        itemEntity.setHsnNo(updatedItem.getHsnNo());
        itemEntity.setConsumables(updatedItem.getConsumables());

        itemEntity.setModifiedBy(user.getId());
        itemEntity.setModifiedDate(LocalDate.now());

        ItemEntity updatedItems = itemRepository.save(itemEntity);
        return itemMapper.mapToItemDto(updatedItems);
    }

    @Transactional
    @Override
    public void deleteItem(Long pharmacyId, UUID itemId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<ItemEntity> itemEntity = itemRepository.findByItemIdAndPharmacyId(itemId, pharmacyId);

        if (itemEntity.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with ID: " + itemId + " for pharmacy ID: " + pharmacyId);
        }

        itemRepository.delete(itemEntity.get());

    }
}
