package com.mes.menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoush
 * @since 2024-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 模块名称
     */
    private String menuName;

    /**
     * 图标
     */
    private String icon;

    /**
     * 地址
     */
    private String url;

    /**
     * 语言类型
     */
    private String languageType;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 排序
     */
    private String listSort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 权限
     */
    private String perms;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;


}
