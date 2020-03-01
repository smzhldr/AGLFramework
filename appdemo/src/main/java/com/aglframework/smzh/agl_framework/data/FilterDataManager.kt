package com.aglframework.smzh.agl_framework.data

import com.aglframework.smzh.agl_framework.R

class FilterDataManager private constructor() {

    companion object {
        private var instance: FilterDataManager? = null
            get() {
                if (field == null) {
                    field = FilterDataManager()
                }
                return field
            }

        @Synchronized
        fun get(): FilterDataManager {
            return instance!!
        }
    }


    private val filterList: ArrayList<Filter> by lazy {
        ArrayList<Filter>().apply {
            add(Filter("origin", "原图", R.drawable.origin))
            add(Filter("nature", "自然", R.drawable.nature))
            add(Filter("bearch", "海滩", R.drawable.bearch))
            add(Filter("icream", "冰淇淋", R.drawable.icream))
            add(Filter("first_love", "初恋", R.drawable.first_love))
            add(Filter("oxygen", "氧气", R.drawable.oxygen))
            add(Filter("coral", "珊瑚", R.drawable.coral))
            add(Filter("forest", "森林", R.drawable.forest))
            add(Filter("fresh", "清晰", R.drawable.fresh))
            add(Filter("glossy", "质感", R.drawable.glossy))
            add(Filter("grass", "草原", R.drawable.grass))
            add(Filter("holiday", "假日", R.drawable.holiday))
            add(Filter("kisskiss", "亲亲", R.drawable.kisskiss))
            add(Filter("lolita", "少女", R.drawable.lolita))
            add(Filter("delicious", "美味", R.drawable.delicious))
            add(Filter("music", "音乐", R.drawable.music))
            add(Filter("clean", "清除", R.drawable.clean))
            add(Filter("pink", "粉红", R.drawable.pink))
            add(Filter("sunset", "夏日", R.drawable.sunset))
            add(Filter("sweety", "蜜桃", R.drawable.sweety))
            add(Filter("sweet", "甜美", R.drawable.sweet))
            add(Filter("urban", "巴黎", R.drawable.urban))
            add(Filter("vintage", "红酒", R.drawable.vintage))
            add(Filter("vivid", "生动", R.drawable.vivid))
            add(Filter("xinxian", "新鲜", R.drawable.xinxian))
            add(Filter("yuanqi", "元气", R.drawable.yuanqi))
            add(Filter("snow", "初雪", R.drawable.snow))
            add(Filter("jugeng", "丁香", R.drawable.jugeng))
            add(Filter("makalong", "银杏", R.drawable.makalong))
        }
    }

    fun getFilterList(): List<Filter> {
        return filterList
    }


}