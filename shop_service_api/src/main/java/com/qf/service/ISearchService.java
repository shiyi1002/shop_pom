package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {
    List<Goods> searchGoods(String keyword);
   int insertSearch(Goods goods);
}
