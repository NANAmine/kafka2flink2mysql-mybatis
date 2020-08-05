package com.flink.entity;

/**
 * @Author LT-0024
 * @Date 2020/7/13 14:58
 * @Version 1.0
 */
public class OrderDetail {

    /**
     * 小票号
     */
    public String billno;
    /**
     * 门店号
     */
    public String mkt;
    /**
     * 单据类别
     */
    public String djlb;
    /**
     * 订单日期
     */
    public String rqsj;
    /**
     * 总金额
     */
    public Double hjzje;
    /**
     * 总折扣
     */
    public Double hjzke;

    /**
     * 身份证号
     */
    public String shgwkh;

    public OrderDetail(String billno, String mkt, String djlb, String rqsj, Double hjzje, Double hjzke,String shgwkh) {
        this.billno = billno;
        this.mkt = mkt;
        this.djlb = djlb;
        this.rqsj = rqsj;
        this.hjzje = hjzje;
        this.hjzke = hjzke;
        this.shgwkh = shgwkh;
    }

    public OrderDetail() {

    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "billno='" + billno + '\'' +
                ", mkt='" + mkt + '\'' +
                ", djlb='" + djlb + '\'' +
                ", rqsj='" + rqsj + '\'' +
                ", hjzje='" + hjzje + '\'' +
                ", hjzke='" + hjzke + '\'' +
                ", shgwkh='" + shgwkh + '\'' +
                '}';
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getMkt() {
        return mkt;
    }

    public void setMkt(String mkt) {
        this.mkt = mkt;
    }

    public String getDjlb() {
        return djlb;
    }

    public void setDjlb(String djlb) {
        this.djlb = djlb;
    }

    public String getRqsj() {
        return rqsj;
    }

    public void setRqsj(String rqsj) {
        this.rqsj = rqsj;
    }

    public Double getHjzje() {
        return hjzje;
    }

    public void setHjzje(Double hjzje) {
        this.hjzje = hjzje;
    }

    public Double getHjzke() {
        return hjzke;
    }

    public void setHjzke(Double hjzke) {
        this.hjzke = hjzke;
    }

    public String getShgwkh() {
        return shgwkh;
    }

    public void setShgwkh(String shgwkh) {
        this.shgwkh = shgwkh;
    }

    @Override
    public boolean equals(Object obj) {
        OrderDetail u = (OrderDetail) obj;
        return billno.equals(u.billno);
    }

    @Override
    public int hashCode() {
        String in = billno;
        return in.hashCode();
    }
}

