package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.entity.Asset;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.service.AssetService;
import com.project.TradingWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
                                                           @PathVariable String coinId,
                                                           @RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        Asset asset = assetService.getAssetByUserIdAndCoinId(user.getId(), coinId);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssetsOfUser(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        UserEntity user = userService.findUserByToken(token);
        List<Asset> assets = assetService.getUserAssets(user.getId());
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

}
