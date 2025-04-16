package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.vo.ChartVO;

import java.util.List;

public interface ViewsService {

    Result<List<ChartVO>> staticControls();

}
