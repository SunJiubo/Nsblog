package top.sunjiubo.springboot.Nsblog.service;

import top.sunjiubo.springboot.Nsblog.model.Catalog;
import top.sunjiubo.springboot.Nsblog.model.User;

import java.util.List;

public interface CatalogService {

    /**
     * 保存分类
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据ID获取Catalog
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取catalog列表
     */

    List<Catalog> listCatalogs(User user);
}
