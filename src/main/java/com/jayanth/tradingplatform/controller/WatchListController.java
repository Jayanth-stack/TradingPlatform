package com.jayanth.tradingplatform.controller;


import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.WatchList;
import com.jayanth.tradingplatform.service.CoinService;
import com.jayanth.tradingplatform.service.UserService;
import com.jayanth.tradingplatform.service.WatchListService;
import com.jayanth.tradingplatform.service.WatchListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());

        return new ResponseEntity<>(watchList, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList (@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        WatchList watchList = watchListService.createWatchList(user);
        return ResponseEntity.ok(watchList);
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(@PathVariable Long watchListId) throws Exception {

        WatchList watchList = watchListService.findById(watchListId);
        return ResponseEntity.ok(watchList);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(@PathVariable String coinId,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Coin coin  = coinService.findById(coinId);

        Coin addedCoin= watchListService.addItemToWatchList(coin, user);

        return ResponseEntity.ok(addedCoin);

    }


}
