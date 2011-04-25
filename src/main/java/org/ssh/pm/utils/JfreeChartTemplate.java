package org.ssh.pm.utils;

import java.awt.Color;
import java.awt.Font;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/***
 * Jfreechart
 *
 * @author 朱湘鄂
 *
 */
public class JfreeChartTemplate {

    // 2D饼图方法
    public static String generatePieChart(DefaultPieDataset dataset, String title, int w, int h) {
        // struts
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // spring
        //http://stackoverflow.com/questions/559155/how-do-i-get-a-httpservletrequest-in-my-spring-beans
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //probably Spring 3.0 for singelton beans required!
        //@Autowired(required=true)
        //private HttpServletRequest request;

        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        // 2D图
        JFreeChart chart = ChartFactory.createPieChart(title, // 图表标题
                dataset, // 数据集
                true, // 是否显示图例
                true, // 是否生成工具
                false // 是否生成URL链接
                );
        chart.setBackgroundPaint(Color.pink);
        try {
            // ------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            // ------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    // 3D饼图方法
    public static String generatePieChart3D(DefaultPieDataset dataset, String title, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        // 3D图
        JFreeChart chart = ChartFactory.createPieChart3D(title, // 图表标题
                dataset, // 数据集
                true, // 是否显示图例
                true, // 是否生成工具
                false // 是否生成URL链接
                );
        chart.setBackgroundPaint(Color.pink);
        try {
            // ------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            // ------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    // 3D水平柱状图
    public static String generateBarChart3D(CategoryDataset dataset, String title, String catalog, String valueaxis,
            int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createBarChart3D(title, // 图表标题
                catalog, //目录轴的显示标签
                valueaxis, //数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.HORIZONTAL, // 图表方向：水平
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
                );
        try {
            // 得到chart的保存路径
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            // 使用printWriter将文件写出
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    // 2D水平柱状图
    public static String generateBarChart2D(CategoryDataset dataset, String title, String catalog, String valueaxis,
            int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createBarChart(title, // 图表标题
                catalog, //目录轴的显示标签
                valueaxis, //数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.HORIZONTAL, // 图表方向：水平
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
                );
        try {
            // 得到chart的保存路径
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            // 使用printWriter将文件写出
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    // 3D纵向柱状图
    public static String generateBarChartHeng3D(CategoryDataset dataset, String title, String catalog,
            String valueaxis, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createBarChart3D(title, // 图表标题
                catalog, //目录轴的显示标签
                valueaxis, //数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：纵向
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
                );
        try {
            // 得到chart的保存路径
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            // 使用printWriter将文件写出
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    // 2D纵向柱状图
    public static String generateBarChartHeng2D(CategoryDataset dataset, String title, String catalog,
            String valueaxis, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createBarChart(title, // 图表标题
                catalog, //目录轴的显示标签
                valueaxis, //数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平
                true, // 是否显示图例(对于简单的柱状图必须是false)
                false, // 是否生成工具
                false // 是否生成URL链接
                );
        try {
            // 得到chart的保存路径
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            // 使用printWriter将文件写出
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //区域图
    public static String generateAddressChart(CategoryDataset createDataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createAreaChart(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                createDataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        @SuppressWarnings("unused")
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //3D纵向堆栈图
    public static String generateStockChartZong3D(CategoryDataset dataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createStackedBarChart3D(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //3D横向堆栈图
    public static String generateStockChartHeng3D(CategoryDataset dataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createStackedBarChart3D(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.HORIZONTAL, //定义区域图的方向为横向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //2D纵向堆栈图
    public static String generateStockChartZong2D(CategoryDataset dataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createStackedBarChart(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //2D横向堆栈图
    public static String generateStockChartHeng2D(CategoryDataset dataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createStackedBarChart(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.HORIZONTAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //2D线性图
    public static String generateLineChart2D(CategoryDataset dataset, String title, String hengTitle, String zongtitle,
            int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createLineChart(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接
        //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //2D线性图  纵轴不从0起
    public static String generateLineChart2Ds(CategoryDataset dataset, String title, String hengTitle,
            String zongtitle, int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createLineChart(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();//获得 plot：3dBar为CategoryPlot
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        //numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setAutoRangeIncludesZero(false);//设置Y轴的数据自动范围，不从0开始
        numberaxis.setAutoTickUnitSelection(false); //不自动设置刻度
        numberaxis.setAutoRangeMinimumSize(0.5d);
        //numberaxis.seta
        NumberTickUnit ntu = new NumberTickUnit(0.1d); //设置Y轴的刻度
        numberaxis.setTickUnit(ntu);
        //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }

    //3D线性图
    public static String generateLineChart3D(CategoryDataset dataset, String title, String hengTitle, String zongtitle,
            int w, int h) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        String filename = "";
        String fname = "";
        JFreeChart chart = ChartFactory.createLineChart3D(title, //图表标题
                hengTitle, //横轴标题
                zongtitle, //纵轴标题
                dataset, //绘图数据集
                PlotOrientation.VERTICAL, //定义区域图的方向为纵向
                true, //是否显示图例标识
                true, //是否显示tooltips
                false); //是否支持超链接          //设置标题字体
        chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
        //设置图例类别字体
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 15));
        //设置背景色
        chart.setBackgroundPaint(new Color(160, 214, 248));
        //获取绘图区对象
        CategoryPlot plot = chart.getCategoryPlot();
        try {
            //------得到chart的保存路径----
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsJPEG(chart, w, h, info, session);
            //------使用printWriter将文件写出----
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        fname = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
        return fname;
    }
}