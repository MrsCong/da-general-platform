package com.dgp.common.utils;


import com.dgp.common.vo.IdCardVO;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 2、地址码(前六位数） 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S = Sum(Ai Wi), i = 0, , 16 ，先对前17位数字的权求和 ;
 * Ai:表示第i位置上的身份证号码数字值; Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 （2）计算模 Y = mod(S,
 * 11) （3）通过模( 0 1 2 3 4 5 6 7 8 9 10)得到对应的校验码 Y:1 0 X 9 8 7 6 5 4 3 2
 *
 * @author quanmin
 * @since 2020-06-03 16:42
 */
public class IDCardUtils {

    final static Map<Integer, String> zoneNum = new HashMap<>();

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "宁夏");
        zoneNum.put(65, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    private final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private final static int IDCARD_LEN_OLD = 15;
    private final static int IDCARD_LEN_NEW = 18;

    /**
     * 身份证验证
     *
     * @param certNo 号码内容
     * @return 是否有效 null和"" 都是false
     */
    public static boolean isIDCard(String certNo) {
        if (certNo == null || (certNo.length() != IDCARD_LEN_OLD
                && certNo.length() != IDCARD_LEN_NEW)) {
            return false;
        }

        //校验位数
        final char[] cs = certNo.toUpperCase().toCharArray();
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X') {
                break;//最后一位可以 是X或x
            }
            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }

        //校验区位码
        if (!zoneNum.containsKey(Integer.valueOf(certNo.substring(0, 2)))) {
            return false;
        }

        //校验生日
        if (certNo.length() == IDCARD_LEN_OLD) {
            if (!checkIdcardCalendar15(certNo)) {
                return false;
            }
        }
        if (certNo.length() == IDCARD_LEN_NEW) {
            if (!checkIdcardCalendar18(certNo)) {
                return false;
            }
        }

        //校验"校验码"
        if (certNo.length() == IDCARD_LEN_OLD) {
            return true;
        }

        return cs[cs.length - 1] == PARITYBIT[power % 11];
    }

    /**
     * 检查15位身份证的生日是否合法
     */
    private static boolean checkIdcardCalendar15(String certNo) {
        // 获取出生年月日
        String birthday = certNo.substring(6, 12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formatDay;
        //生日部分无法转化或是转化异常 身份证错误
        try {
            LocalDate birthDay = LocalDate.parse(birthday, formatter);
            formatDay = formatter.format(birthDay);
            //可以转化 确定是否还是同一天
            if (!formatDay.equals(birthday)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 检查18位身份证的生日是否合法
     */
    private static boolean checkIdcardCalendar18(String certNo) {
        // 获取出生年月日
        String birthday = certNo.substring(6, 14);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatDay;
        //生日部分无法转化或是转化异常 身份证错误
        try {
            LocalDate birthDay = LocalDate.parse(birthday, formatter);
            formatDay = formatter.format(birthDay);
            //判断年份 不能小于1900 不能大于当前
            if (birthDay.getYear() <= 1900 && birthDay.getYear() > LocalDate.now().getYear()) {
                return false;
            }
            //可以转化 确定是否还是同一天
            if (!formatDay.equals(birthday)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 通过身份证号码获取出生日期、性别、年龄
     *
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static IdCardVO getBirAgeSex(String certificateNo) {
        if (StringUtils.isNotEmpty(certificateNo)) {
            throw new NullPointerException("身份证信息错误");
        }
        String birthday = "";
        String age = "";
        String sexCode = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (char c : number) {
                if (!flag) {
                    return new IdCardVO();
                }
                flag = Character.isDigit(c);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) {
                    return new IdCardVO();
                }
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(
                    certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2
                    == 0 ? "F" : "M";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(
                    certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1))
                    % 2 == 0 ? "F" : "M";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }

        IdCardVO idCardInfo = new IdCardVO();
        idCardInfo.setBirthday(DateUtil.getLocalDate(birthday));
        idCardInfo.setAge(Integer.parseInt(age));
        idCardInfo.setSexCode(sexCode);
        return idCardInfo;
    }

    public static Integer getAgeBirthday(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate now = LocalDate.now();
        return date.until(now).getYears();
    }

    public static String toUpperCaseCertNo(String certNo) {
        if (StringUtils.isNotEmpty(certNo)) {
            return certNo.toUpperCase();
        }
        return certNo;
    }
}
