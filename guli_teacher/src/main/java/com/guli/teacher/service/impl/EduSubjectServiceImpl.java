package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.mapper.EduSubjectMapper;
import com.guli.teacher.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-06-03
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<String> importEXCL(MultipartFile file) {

        //存储错误信息
        List<String> meg = new ArrayList<>();

        try {
            //1.获取文件流
            InputStream inputStream = file.getInputStream();
            //2.根据流创建workbook
            Workbook workbook = new XSSFWorkbook(inputStream);
            //3.获取Sheet, getSheetAt(0)
            Sheet sheet = workbook.getSheetAt(0);
            //4.根据Sheet获取行数
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum <= 1) {
                meg.add("请填写数据");
                return meg;
            }
            //5.遍历行
            for (int rowNum = 1; rowNum < lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                //6.获取每一行的第一列：一级分类
                Cell cell = row.getCell(0);
                if (cell == null) {
                    meg.add("第" + rowNum + "行第1列为空");
                    continue;
                }
                String cellValue = cell.getStringCellValue();
                if (StringUtils.isEmpty(cellValue)) {
                    meg.add("第" + rowNum + "行第1列数据为空");
                    continue;
                }
                //7.判断列是否存在，存在获取列的数据
                EduSubject subject = this.selectSubjectByName(cellValue);
                String pid = null;
                //8.把第一列的数据保存到数据库中
                if (subject == null) {
                    //9.保存之前判断此一级分裂是否存在，如果存在，不再添加
                    EduSubject subject1 = new EduSubject();
                    subject1.setTitle(cellValue);
                    subject1.setParentId("0");
                    subject1.setSort(0);
                    baseMapper.insert(subject1);
                    pid = subject1.getId();
                } else {
                    pid = subject.getId();
                }

                //10.再获取每一行的第二列
                Cell cell1 = row.getCell(1);
                //11.获取第二列中的数据（二级分类）
                if (cell1 == null) {
                    meg.add("第" + rowNum + "行第2列为空");
                    continue;
                }
                String stringCellValue = cell1.getStringCellValue();
                if (StringUtils.isEmpty(stringCellValue)) {
                    meg.add("第" + rowNum + "行第2列数据为空");
                    continue;
                }
                //12.判断此一级分类中是否存在此二级分类
                EduSubject subject2 = this.selectSubjectByNameAndParentId(stringCellValue, pid);

                //13.如果此一级分类中有此二级分类，不保存
                if (subject2 == null) {
                    EduSubject subject1 = new EduSubject();
                    subject1.setTitle(stringCellValue);
                    subject1.setParentId(pid);
                    subject1.setSort(0);
                    baseMapper.insert(subject1);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return meg;
    }

    /**
     * 根据二级分类名称和id查询
     *
     * @param stringCellValue
     * @param pid
     * @return
     */
    private EduSubject selectSubjectByNameAndParentId(String stringCellValue, String pid) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", stringCellValue);
        wrapper.eq("parent_id", pid);
        EduSubject subject = baseMapper.selectOne(wrapper);
        return subject;
    }

    /**
     * 根据课程一级分类的名字查询是否存在
     *
     * @param cellValue
     * @return
     */
    private EduSubject selectSubjectByName(String cellValue) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", cellValue);
        wrapper.eq("parent_id", 0);
        EduSubject subject = baseMapper.selectOne(wrapper);
        return subject;
    }
}
