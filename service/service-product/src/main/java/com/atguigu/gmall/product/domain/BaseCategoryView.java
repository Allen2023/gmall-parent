package com.atguigu.gmall.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName base_category_view
 */
@TableName(value ="base_category_view")
@Data
public class BaseCategoryView implements Serializable {
    /**
     * 编号
     */
    private Long id;

    /**
     * 编号
     */
    private Long category1Id;

    /**
     * 分类名称
     */
    private String category1Name;

    /**
     * 编号
     */
    private Long category2Id;

    /**
     * 二级分类名称
     */
    private String category2Name;

    /**
     * 编号
     */
    private Long category3Id;

    /**
     * 三级分类名称
     */
    private String category3Name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BaseCategoryView other = (BaseCategoryView) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCategory1Id() == null ? other.getCategory1Id() == null : this.getCategory1Id().equals(other.getCategory1Id()))
            && (this.getCategory1Name() == null ? other.getCategory1Name() == null : this.getCategory1Name().equals(other.getCategory1Name()))
            && (this.getCategory2Id() == null ? other.getCategory2Id() == null : this.getCategory2Id().equals(other.getCategory2Id()))
            && (this.getCategory2Name() == null ? other.getCategory2Name() == null : this.getCategory2Name().equals(other.getCategory2Name()))
            && (this.getCategory3Id() == null ? other.getCategory3Id() == null : this.getCategory3Id().equals(other.getCategory3Id()))
            && (this.getCategory3Name() == null ? other.getCategory3Name() == null : this.getCategory3Name().equals(other.getCategory3Name()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCategory1Id() == null) ? 0 : getCategory1Id().hashCode());
        result = prime * result + ((getCategory1Name() == null) ? 0 : getCategory1Name().hashCode());
        result = prime * result + ((getCategory2Id() == null) ? 0 : getCategory2Id().hashCode());
        result = prime * result + ((getCategory2Name() == null) ? 0 : getCategory2Name().hashCode());
        result = prime * result + ((getCategory3Id() == null) ? 0 : getCategory3Id().hashCode());
        result = prime * result + ((getCategory3Name() == null) ? 0 : getCategory3Name().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", category1Id=").append(category1Id);
        sb.append(", category1Name=").append(category1Name);
        sb.append(", category2Id=").append(category2Id);
        sb.append(", category2Name=").append(category2Name);
        sb.append(", category3Id=").append(category3Id);
        sb.append(", category3Name=").append(category3Name);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}