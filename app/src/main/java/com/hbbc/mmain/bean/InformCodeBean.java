package com.hbbc.mmain.bean;

import java.util.List;

/**
 * 城市编号实体类
 */
public class InformCodeBean {

    /**
     * CityName : 北京
     * ProvinceCode : 010
     * CityCode : 010
     */

    private List<DtCity> dtCity;
    /**
     * ProvinceCode : 010
     * ProvinceName : 北京
     */

    private List<DtSheng> dtSheng;

    public List<DtCity> getDtCity() {
        return dtCity;
    }

    public void setDtCity(List<DtCity> dtCity) {
        this.dtCity = dtCity;
    }

    public List<DtSheng> getDtSheng() {
        return dtSheng;
    }

    public void setDtSheng(List<DtSheng> dtSheng) {
        this.dtSheng = dtSheng;
    }

    public static class DtCity {
        private String CityName;
        private String ProvinceCode;
        private String CityCode;

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }

        public String getProvinceCode() {
            return ProvinceCode;
        }

        public void setProvinceCode(String ProvinceCode) {
            this.ProvinceCode = ProvinceCode;
        }

        public String getCityCode() {
            return CityCode;
        }

        public void setCityCode(String CityCode) {
            this.CityCode = CityCode;
        }
    }

    public static class DtSheng {
        private String ProvinceCode;
        private String ProvinceName;

        public String getProvinceCode() {
            return ProvinceCode;
        }

        public void setProvinceCode(String ProvinceCode) {
            this.ProvinceCode = ProvinceCode;
        }

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String ProvinceName) {
            this.ProvinceName = ProvinceName;
        }
    }
}
