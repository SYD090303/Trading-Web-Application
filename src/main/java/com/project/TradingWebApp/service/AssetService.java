package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.Asset;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.entity.UserEntity;

import java.util.List;

public interface AssetService {
    Asset createAsset(UserEntity user, Coin coin, double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserId(Long userId, Long assetId);
    List<Asset> getUserAssets(Long userId);
    Asset updateAsset(Long assetId, double quantity);
    Asset getAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);


}
