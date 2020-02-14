/**
 * Project Name:InfectStatistic-main2
 * File Name:InflectInfo.java
 * Package Name:
 * Date:下午4:03:05
 * Copyright (c) 2020, Doris All Rights Reserved.
 *
*/

/**
 * Description: <br/>
 * Date: 下午4:03:05 <br/>
 * 
 * @author Doris
 * @version
 * @see
 */
public class InflectInfo {
    String area;

    int ip;

    int sp;

    int cure;

    int dead;

    @Override
    public String toString() {
        return "InflectInfo [area=" + area + ", ip=" + ip + ", sp=" + sp + ", cure=" + cure + ", dead=" + dead + "]";
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public void setCure(int cure) {
        this.cure = cure;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public String getArea() {
        return area;
    }

    public int getIp() {
        return ip;
    }

    public int getSp() {
        return sp;
    }

    public int getCure() {
        return cure;
    }

    public int getDead() {
        return dead;
    }

}
