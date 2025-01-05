package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.Asset;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return null;
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return List.of();
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        return null;
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return null;
    }

    @Override
    public void deleteAsset(Long assetId) {

    }
}
