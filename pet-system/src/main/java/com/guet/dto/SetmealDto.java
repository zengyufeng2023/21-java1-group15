package com.guet.dto;

import com.guet.entity.Setmeal;
import com.guet.entity.SetmealProduct;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealProduct> setmealProducts;

    private String categoryName;
}
