package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.Asset;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.AssetRepository;
import com.project.TradingWebApp.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(UserEntity user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId).orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Override
    public Asset getAssetByUserId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        Asset oldAsset = getAssetById(assetId);

        System.out.println("Before Update - Asset ID: " + assetId + ", Old Quantity: " + oldAsset.getQuantity() + ", Change: " + quantity);

        double newQuantity = oldAsset.getQuantity() + quantity;
        oldAsset.setQuantity(newQuantity);

        System.out.println("After Update - Asset ID: " + assetId + ", New Quantity: " + oldAsset.getQuantity());

        return assetRepository.save(oldAsset);
    }


    @Override
    public Asset getAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);
    }
}
