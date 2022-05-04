package com.lin.baselib.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class JsonBean implements IPickerViewData {

    private String name;
    private String code;
    private String id;
    private String type;
    private List<JsonBean> children;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<JsonBean> getChildren() {
        return children;
    }

    public void setChildren(List<JsonBean> children) {
        this.children = children;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
