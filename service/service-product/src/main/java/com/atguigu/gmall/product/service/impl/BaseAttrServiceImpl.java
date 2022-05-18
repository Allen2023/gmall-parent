package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 8:58
 */
@Service
public class BaseAttrServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id, category2Id, category3Id);
    }

    @Transactional
    @Override
    public void saveAttrInfoAndValue(BaseAttrInfo baseAttrInfo) {
        //1.保存属性名信息到base_attr_info
        baseAttrInfoMapper.insert(baseAttrInfo);
        //2.保存属性值信息到base_attr_value
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            //回填平台属性id
            Long id = baseAttrInfo.getId();
            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }


    @Override
    public List<BaseAttrValue> findAttrValueById(Long attrId) {
        List<BaseAttrValue> attrValueList = baseAttrValueMapper.selectList(new LambdaQueryWrapper<BaseAttrValue>().eq(BaseAttrValue::getAttrId, attrId));
        return attrValueList;
    }

    @Override
    public void saveOrUpdateAttrInfoAndValue(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() == null) {
            saveAttrInfoAndValue(baseAttrInfo);
        } else {
            updateAttrInfoAndValue(baseAttrInfo);
        }
    }

    @Transactional
    @Override
    public void updateAttrInfoAndValue(BaseAttrInfo baseAttrInfo) {
        //1.保存属性名信息到base_attr_info
        baseAttrInfoMapper.updateById(baseAttrInfo);
        List<Long> notdeleteIds = new ArrayList<>();
        for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValueList()) {
            Long id = baseAttrValue.getId();
            notdeleteIds.add(id);
        }
        //如果list中没有id返回证明该平台属性下的属性值全部删除
        if (notdeleteIds.size() > 0) {
            //list中有id
            QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id", baseAttrInfo.getId());
            wrapper.notIn("id", notdeleteIds);
            baseAttrValueMapper.delete(wrapper);
        } else {
            //list中无id
            QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id", baseAttrInfo.getId());
            baseAttrValueMapper.delete(wrapper);
        }
        //2.保存属性值信息到base_attr_value
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            if (baseAttrValue.getId() == null) {
                //如果属性值id为空则为新增操作 将attr_id回填并新增
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            } else {
                //id不为空,为更新操作
                baseAttrValueMapper.updateById(baseAttrValue);
            }
        }
    }


}
