package com.user.us.service.userInfo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.*;
import ar.com.fdvs.dj.domain.builders.*;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//http://dynamicjasper.com/2010/10/05/grouping-styles/
public class UserInfoReport {
    private final Collection<UserInfo> list;

    public UserInfoReport(Collection<UserInfo> c) {
        list = new ArrayList<>(c);
    }

    public JasperPrint getReport() throws ColumnBuilderException, JRException, ClassNotFoundException {
        Style headerStyle = createHeaderStyle();
        Style detailTextStyle = createDetailTextStyle();
        Style detailNumberStyle = createDetailNumberStyle();
        DynamicReport dynaReport = getReport(headerStyle, detailTextStyle, detailNumberStyle);

        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), new JRBeanCollectionDataSource(list));
        return jp;
    }

    private Style createHeaderStyle() {
        return new StyleBuilder(true)
                //.setFont(Font.ARIAL_MEDIUM_BOLD)
                .setBorder(Border.THIN())
                .setBorderBottom(Border.PEN_2_POINT())
                .setBorderColor(Color.BLACK)
                .setBackgroundColor(Color.ORANGE)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setTransparency(Transparency.OPAQUE)
                .build();
    }

    private Style createDetailTextStyle() {
        return new StyleBuilder(true)
                //.setFont(Font.ARIAL_MEDIUM_BOLD)
                .setBorder(Border.DOTTED())
                .setBorderColor(Color.BLACK)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.LEFT)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPaddingLeft(5)
                .build();
    }

    private Style createDetailNumberStyle() {
        return new StyleBuilder(true)
                //.setFont(Font.ARIAL_MEDIUM_BOLD)
                .setBorder(Border.DOTTED())
                .setBorderColor(Color.BLACK)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.RIGHT)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPaddingRight(5)
                .setPattern("#,##0.00")
                .build();
    }

    private AbstractColumn createColumn(String property, Class<?> type, String title, int width, Style headerStyle, Style detailStyle)
            throws ColumnBuilderException {
        return ColumnBuilder.getNew()
                .setColumnProperty(property, type.getName())
                .setTitle(title)
                .setWidth(Integer.valueOf(width))
                .setStyle(detailStyle)
                .setHeaderStyle(headerStyle)
                .build();
    }

    private DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle)
            throws ColumnBuilderException, ClassNotFoundException {

        DynamicReportBuilder report = new DynamicReportBuilder();
        report.setProperty("net.sf.jasperreports.awt.ignore.missing.font" , "true");


        AbstractColumn columnUserName = createColumn("username", String.class, "User Name", 50, headerStyle, detailTextStyle);
        AbstractColumn columnPassword = createColumn("password", String.class, "Password", 30, headerStyle, detailTextStyle);
        report.addColumn(columnUserName).addColumn(columnPassword);

        StyleBuilder titleStyle = new StyleBuilder(true);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setFont(new Font(20, null, true));
        // you can also specify a font from the classpath, eg:
        // titleStyle.setFont(new Font(20, "/fonts/someFont.ttf", true));

        StyleBuilder subTitleStyle = new StyleBuilder(true);
        subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        subTitleStyle.setFont(new Font(Font.MEDIUM, null, true));

        report.setTitle("User Report");
        report.setTitleStyle(titleStyle.build());
        //report.setSubtitle("Commission received by Employee");
        //report.setSubtitleStyle(subTitleStyle.build());

        Style headerVariables = new Style();
        //headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        report.setPrintColumnNames(false);
        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columnUserName)
               .addFooterVariable(columnUserName , DJCalculation.NOTHING,headerVariables)
               .addFooterVariable(columnPassword,DJCalculation.NOTHING,headerVariables)
               .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME) // set layout style
               .build();
       report.addGroup(g1);
        DJChartBuilder cb = new DJChartBuilder();
        DJChart chart = cb.setType(DJChart.BAR_CHART)
                .setOperation(DJChart.CALCULATION_SUM)
                .setColumnsGroup(g1)
                .addColumn(columnPassword)
                .setPosition(DJChartOptions.POSITION_HEADER)
                .setShowLabels(true)
                .build();

        report.addChart(chart);

        report.addAutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT);
        report.setUseFullPageWidth(true);
        return report.build();
    }
}
