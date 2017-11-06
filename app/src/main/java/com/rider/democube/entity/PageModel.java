package com.rider.democube.entity;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

/**
 * Created by wubin on 2017/11/6 0006.
 */

public class PageModel {

    @LayoutRes int sampleLayoutRes;
    @LayoutRes int practiceLayoutRes;
    @StringRes int titleRes;

    public PageModel(@LayoutRes int sampleLayoutRes,@StringRes int titleRes, @LayoutRes int practiceLayoutRes) {

        this.sampleLayoutRes = sampleLayoutRes;
        this.titleRes = titleRes;
        this.practiceLayoutRes = practiceLayoutRes;
    }


    public int getSampleLayoutRes() {
        return sampleLayoutRes;
    }

    public void setSampleLayoutRes(int sampleLayoutRes) {
        this.sampleLayoutRes = sampleLayoutRes;
    }

    public int getPracticeLayoutRes() {
        return practiceLayoutRes;
    }

    public void setPracticeLayoutRes(int practiceLayoutRes) {
        this.practiceLayoutRes = practiceLayoutRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }
}
