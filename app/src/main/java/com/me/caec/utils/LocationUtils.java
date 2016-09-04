package com.me.caec.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.me.caec.bean.Location;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 从json中查找省市区
 * <p/>
 * Created by yin on 2016/9/2.
 */
public class LocationUtils {

    /**
     * 获取全部省市区数据
     *
     * @param ctx
     * @return
     * @throws IOException
     */
    public static List<Location.DataBean> getLocation(Context ctx) {
        InputStream in = null;
        try {
            in = ctx.getAssets().open("location.min.json");
            String jsonString = StreamUtils.stream2String2(in);
            Location location = JSON.parseObject(jsonString, Location.class);
            return location.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据省id查找市列表
     *
     * @param location
     * @param provinceId
     * @return
     */
    public static List<Location.DataBean.CityBean> findCityBeanWithProvinceId(List<Location.DataBean> location, int provinceId) {

        for (Location.DataBean dataBean : location) {
            if (dataBean.getAreaid() == provinceId) {
                return dataBean.getAl();
            }
        }

        return null;
    }

    /**
     * 根据市id查找区/县列表
     *
     * @param cities
     * @param cityId
     * @return
     */
    public static List<Location.DataBean.CityBean.AreaBean> findAreaBeanWithcityId(List<Location.DataBean.CityBean> cities, int cityId) {

        for (Location.DataBean.CityBean cityBean : cities) {
            if (cityBean.getAreaid() == cityId) {
                return cityBean.getAl();
            }
        }

        return null;
    }

    /**
     * 根据省id查找省名字
     *
     * @param id
     * @return
     */
    public static String findProvinceNameWithId(Context ctx, int id) {
        List<Location.DataBean> location = getLocation(ctx);
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                if (dataBean.getAreaid() == id) {
                    return dataBean.getAreaname();
                }
            }
        }

        return null;
    }

    public static String findProvinceNameWithId(List<Location.DataBean> location, int id) {
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                if (dataBean.getAreaid() == id) {
                    return dataBean.getAreaname();
                }
            }
        }

        return null;
    }

    /**
     * 根据市id查找市名字
     *
     * @param id
     * @return
     */
    public static String findCityNameWithId(Context ctx, int id) {
        List<Location.DataBean> location = getLocation(ctx);
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                List<Location.DataBean.CityBean> cities = dataBean.getAl();

                for (Location.DataBean.CityBean cityBean : cities) {
                    if (cityBean.getAreaid() == id) {
                        return cityBean.getAreaname();
                    }
                }
            }
        }

        return null;
    }

    public static String findCityNameWithId(List<Location.DataBean> location, int id) {
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                List<Location.DataBean.CityBean> cities = dataBean.getAl();

                for (Location.DataBean.CityBean cityBean : cities) {
                    if (cityBean.getAreaid() == id) {
                        return cityBean.getAreaname();
                    }
                }
            }
        }

        return null;
    }

    /**
     * 根据区id查找区名字
     *
     * @param id
     * @return
     */
    public static String findAreaNameWithId(Context ctx, int id) {
        List<Location.DataBean> location = getLocation(ctx);
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                List<Location.DataBean.CityBean> cities = dataBean.getAl();

                for (Location.DataBean.CityBean cityBean : cities) {
                    List<Location.DataBean.CityBean.AreaBean> areas = cityBean.getAl();

                    for (Location.DataBean.CityBean.AreaBean areaBean : areas) {
                        if (areaBean.getAreaid() == id) {
                            return areaBean.getAreaname();
                        }
                    }
                }

            }
        }

        return null;
    }

    public static String findAreaNameWithId(List<Location.DataBean> location, int id) {
        if (location != null) {
            for (Location.DataBean dataBean : location) {
                List<Location.DataBean.CityBean> cities = dataBean.getAl();

                for (Location.DataBean.CityBean cityBean : cities) {
                    List<Location.DataBean.CityBean.AreaBean> areas = cityBean.getAl();

                    for (Location.DataBean.CityBean.AreaBean areaBean : areas) {
                        if (areaBean.getAreaid() == id) {
                            return areaBean.getAreaname();
                        }
                    }
                }

            }
        }

        return null;
    }
}
