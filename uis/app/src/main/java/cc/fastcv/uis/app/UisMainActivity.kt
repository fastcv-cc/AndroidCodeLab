package cc.fastcv.uis.app

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.uis.app.bezier_curve.BezierCurveActivity
import cc.fastcv.uis.app.compass.CompassActivity
import cc.fastcv.uis.app.corner_frame_layout.CornerFrameLayoutActivity
import cc.fastcv.uis.app.date_switch_view.DateSwitchViewActivity
import cc.fastcv.uis.app.draw_spell.DrawSpellActivity
import cc.fastcv.uis.app.histogram.HistogramViewActivity
import cc.fastcv.uis.app.inscribed_circle.InscribedCircleActivity
import cc.fastcv.uis.app.marquee.MarqueeTextViewActivity
import cc.fastcv.uis.app.overlapping_judge.OverlappingJudgeActivity
import cc.fastcv.uis.app.path_measure.PathMeasureActivity
import cc.fastcv.uis.app.radar_view.RadarViewActivity
import cc.fastcv.uis.app.round_corner_progress_bar.RoundCornerProgressBarActivity
import cc.fastcv.uis.app.round_progress_bar.RoundProgressBarActivity
import cc.fastcv.uis.app.simple_number_clock.SimpleNumberClockActivity
import cc.fastcv.uis.app.star.StarActivity
import cc.fastcv.uis.app.text.TextActivity
import cc.fastcv.uis.app.theoretical_basis.TheoreticalBasisActivity
import cc.fastcv.uis.app.turntable.TurntableActivity
import cc.fastcv.uis.app.xfermode.XfermodeActivity

class UisMainActivity : TreeActivity() {

    override fun buildLibItemList(): List<LibItem> {

        return listOf(
            LibItem(
                "UI理论知识",
                "自定义UI必备理论知识",
                Intent(this, TheoreticalBasisActivity::class.java)
            ), LibItem(
                "重合判断",
                "重合判断",
                Intent(this, OverlappingJudgeActivity::class.java)
            ), LibItem(
                "着色器实验",
                "着色器实验",
                Intent(this, DrawSpellActivity::class.java)
            ), LibItem(
                "刮刮乐",
                "Xfermode功能测试",
                Intent(this, XfermodeActivity::class.java)
            ), LibItem(
                "画星星",
                "画星星",
                Intent(this, StarActivity::class.java)
            ), LibItem(
                "刻度转盘",
                "刻度转盘，带回弹效果",
                Intent(this, TurntableActivity::class.java)
            ), LibItem(
                "带圆角的FrameLayout",
                "带圆角的FrameLayout",
                Intent(this, CornerFrameLayoutActivity::class.java)
            ), LibItem(
                "带圆角的ProgressBar",
                "带圆角的ProgressBar",
                Intent(this, RoundCornerProgressBarActivity::class.java)
            ), LibItem(
                "贝塞尔曲线",
                "贝塞尔曲线",
                Intent(this, BezierCurveActivity::class.java)
            ), LibItem(
                "雷达图",
                "雷达图",
                Intent(this, RadarViewActivity::class.java)
            ), LibItem(
                "Path测量",
                "Path测量的应用",
                Intent(this, PathMeasureActivity::class.java)
            ), LibItem(
                "文本显示",
                "文本显示",
                Intent(this, TextActivity::class.java)
            ), LibItem(
                "外切圆效果",
                "外切圆效果",
                Intent(this, InscribedCircleActivity::class.java)
            ), LibItem(
                "跑马灯",
                "跑马灯",
                Intent(this, MarqueeTextViewActivity::class.java)
            ), LibItem(
                "极简数字时钟",
                "很好看的线段时钟",
                Intent(this, SimpleNumberClockActivity::class.java)
            ), LibItem(
                "指南针",
                "简约指南针效果，配合手机地磁",
                Intent(this, CompassActivity::class.java)
            ), LibItem(
                "日期选择器",
                "日期选择器",
                Intent(this, DateSwitchViewActivity::class.java)
            ), LibItem(
                "直方图图表",
                "直方图图表",
                Intent(this, HistogramViewActivity::class.java)
            ), LibItem(
                "圆角圆形进度条",
                "自定义圆角圆形进度条，支持动态设置进度颜色，进度宽度，最大最小值等等",
                Intent(this, RoundProgressBarActivity::class.java)
            )
        )
    }
}