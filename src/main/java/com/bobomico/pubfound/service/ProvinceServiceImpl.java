// package com.bobomico.pubfound.service;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import com.bobomico.pubfound.annotation.DataSourceType;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// /**
//  * @ClassName: com.bobomico.pubfound.service.mall-bobomico-B
//  * @URL:
//  * @Author: DELL
//  * @Date: 2019/5/1  7:42
//  * @Description:
//  * @version:
//  */
// // todo
// @DataSourceType(value="gfwDataSource")
// @Service
// public class ProvinceServiceImpl extends AbstractRpcService implements ProvinceService {
//
//     @Autowired
//     private SysProvinceDao sysProvinceDao;
//
//     @Override
//     public SysProvinceListStruct getProvinceList() {
//
//         List<SysProvinceDTO> list = sysProvinceDao.getProvinceList();
//
//         return beanToStruct(list);
//     }
//
//     /***
//      *
//      * Project Name: gfw-public-foundation-impl
//      * <p>将dto对象封装struct对象
//      *
//      * @author youqiang.xiong
//      * @date 2018年5月28日  下午3:31:42
//      * @version v1.0
//      * @since
//      * @param provinceList
//      *           省份列表dto
//      * @return  省份列表struct
//      */
//     private SysProvinceListStruct beanToStruct(List<SysProvinceDTO> provinceList){
//
//         if(provinceList == null || provinceList.size() == 0){
//             return null;
//         }
//
//         List<SysProvinceStruct> resultList = new ArrayList<SysProvinceStruct>();
//         for(SysProvinceDTO dto:provinceList){
//             SysProvinceStruct struct = new SysProvinceStruct();
//             struct.provinceId = dto.getProvinceId();
//             struct.provinceName = dto.getProvinceName();
//             resultList.add(struct);
//         }
//
//         SysProvinceListStruct rsStruct = new SysProvinceListStruct(resultList);
//         return rsStruct;
//     }
// }
//
// // EOF