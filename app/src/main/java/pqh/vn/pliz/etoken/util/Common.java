package pqh.vn.pliz.etoken.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Common {

    public static Properties loadProperty(String pathfile) throws Exception {
        Properties configProp = null;

        File file = new File(pathfile);
        if (!file.exists()) {
            throw new Exception("Khong load duoc properties theo duong dan: " + pathfile);
        }

        FileInputStream in = new FileInputStream(file);
        configProp = new Properties();
        configProp.load(in);
        in.close();

        return configProp;
    }

    public static boolean isNumeric(String str) throws Exception {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * *
     *
     * @param value
     * @param pattern Vi du: ###,###,###,###,###.###
     * @return
     */
    public static String getFormatCurrency(double value, String pattern) throws Exception {
        return new DecimalFormat(pattern).format(value);
    }

    public static String formatNumber(double value, String currency) {
        if (currency == null) {
            currency = "vnd";
        }
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        if (currency.equalsIgnoreCase("vnd")) {

            return new DecimalFormat("###,###,###,###,###", otherSymbols).format(value);
        } else if (currency.equalsIgnoreCase("usd")) {
            return new DecimalFormat("###,###,###,###,##0.00", otherSymbols).format(value);
        } else if (currency.equalsIgnoreCase("xau")) {
            return new DecimalFormat("###,###,###,###,##0.0000", otherSymbols).format(value);
        } else if (currency.equalsIgnoreCase("rate")) {
            return new DecimalFormat("###,##0.00", otherSymbols).format(value);
        } else {
            return new DecimalFormat("###,###,###,###,###.##", otherSymbols).format(value);
        }
    }

    public static String formatCurrency(double value, String currency) {
        if (currency == null) {
            currency = "VND";
        }
        return formatNumber(value, currency) + " " + currency;
    }

    public static String formatCurrency(double value) {
        return formatCurrency(value,"VND") ;
    }

    /**
     * //* @param format dd/MM/yy HH:mm:ss
     *
     * @return
     */
    public static String getDate(String pattern) throws Exception {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static Date toDate(String strdate, String pattern) throws Exception {
        if (isEmpty(strdate)) {
            return null;
        }
        return new SimpleDateFormat(pattern).parse(strdate);
    }

    public static String toStringByDate(Date dDate, String pattern) throws Exception {
        if (dDate == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(dDate);
    }

    public static java.sql.Date toSqlDate(String strdate, String pattern) throws Exception {
        Date d = toDate(strdate, pattern);
        if (d != null) {
            return new java.sql.Date(d.getTime());
        } else {
            return null;
        }
    }

    public static boolean isEmailValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isMobilePhoneValid(String mobilephone) {
        if (mobilephone.length() < 10 && mobilephone.length() > 11) {
            return false;
        }
        return Pattern.compile("^(0)(\\d{9}|\\d{10}|\\d{11}|\\d{12})$").matcher(mobilephone).matches();
    }

    public static boolean isNotSignVietnameseValid(String text) {
        return Pattern.compile("^[_A-Za-z0-9- \\(\\)\\_\\?\\;\\:\\.\\=\\-\\,]*").matcher(text).matches();
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }
    //\\@\\&
    //backup
//    public static boolean isNotSignVietnameseValid(String text) {
//        return Pattern.compile("^[_A-Za-z0-9- \\~\\!\\@\\#\\$\\%\\&\\*\\(\\)\\_\\+\\|\\<\\>\\/\\?\\;\\'\\[\\]\\}\\{\\:\\.\\=\\-\\`\\,]*").matcher(text).matches();
//    }

    public static boolean isEqualsIgnoreCase(String str1, String str2) {
        return !(str1 == null || str2 == null || !str1.equalsIgnoreCase(str2));
    }

    public static String convertMoneyToWordVNE(String strSo) throws Exception {
        if (strSo == null)
            return "";
        String[] chu = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] donvi = {"", "mươi", "trăm", "nghìn", "mươi", "trăm", "triệu", "mươi", "trăm", "tỷ", "mươi", "trăm",
                "nghìn tỷ", "mươi", "trăm", "triệu tỷ", "mươi", "trăm", "tỷ tỷ", "mươi", "trăm", ""};
        String so;
        Long d = Long.parseLong(strSo);
        so = String.valueOf(d);

        int len_so = so.trim().length();
        String kyso, kychu, previous = "", fullchuso = "", strDatach3 = "";
        for (int i = len_so; i > 0; i--) {
            strDatach3 = tach3(chu, so.substring(len_so - i, len_so));

            if (strDatach3 != null) {
                fullchuso = (strDatach3.equals("")) ? (fullchuso + strDatach3) : (fullchuso + strDatach3 + " "
                        + donvi[i - 3] + " ");
                i = i - 2; // sau khi continue, vong lap for giam -1
                continue;
            }

            // 1.234.567.890: Quet tu 1,2,3,4,5,... // phai qua trai
            kyso = String.valueOf(so.charAt(len_so - i));
            if (kyso.equals("1") && (donvi[i - 1].equals("mươi")) && (i > 1)) {
                previous = kychu = "mười";
            } else {
                if (kyso.equals("1")) {
                    if (!donvi[i].equals("mươi")) {
                        kychu = "một";
                    } // do muoi` nhay continue nen phai tao khoang trang
                    else if (previous.equals("mười")) {
                        kychu = " một";
                    } else if (i == len_so) {
                        kychu = "một";
                    } else {
                        kychu = "mốt";
                    }
                    previous = " ";
                } else if (kyso.equals("5")) {
                    if (!donvi[i].equals("mươi")) {
                        kychu = "năm";
                    } else {
                        fullchuso = fullchuso.trim();
                        if (fullchuso.length() == 0) {
                            kychu = "năm";
                        } else {
                            kychu = " lăm";
                        }
                    }
                    previous = " ";
                } else {
                    if (previous.equals("mười")) {
                        if (kyso.equals("0")) {
                            kychu = "";
                        } else if (kyso.equals("5")) {
                            kychu = " lăm";
                        } else {
                            kychu = " " + chu[Integer.parseInt(kyso)];
                        }
                    } else {
                        if (kyso.equals("0")) {
                            kychu = "";
                        } else {
                            kychu = chu[Integer.parseInt(kyso)];
                        }
                    }
                    previous = " ";
                }
            }

            fullchuso = fullchuso + kychu;
            if (previous.equals("mười")) {
                continue;
            }

            // System.out.print(donvi[i - 1] + " ");
            fullchuso = fullchuso.trim() + " " + donvi[i - 1] + " ";
        }
        if (fullchuso != null && !fullchuso.trim().isEmpty()) {
            fullchuso = fullchuso.trim() + " đồng";
        }
        // System.out.println(fullchuso);
        return fullchuso;
    }

    private static String tach3(String[] chu, String so) {
        String strReturn = null;
        if ((int) (so.length() % 3) == 0) // kiem tra phan tach 3 so la dung
        {
            String strTach3 = so.substring(0, 3); // Trai sang phai
            char t = strTach3.charAt(0), c = strTach3.charAt(1), d = strTach3.charAt(2);

            if ((t == '0') && (c == '0') && (d == '0')) {
                strReturn = "";
            } else if ((t == '0') && (c == '0') && (d != '0')) {
                strReturn = "lẻ " + chu[toInt(d)];
            } else if ((t == '0') && (c != '0') && (d == '0')) {
                strReturn = "không trăm ";
                if (c == '1') {
                    strReturn = (strReturn + "mười");
                } else {
                    strReturn = (strReturn + chu[toInt(c)] + " mươi");
                }
            } else if ((t == '0') && (c != '0') && (d != '0')) {
                strReturn = "không trăm";

                String sNam = chu[toInt(d)];
                if (sNam.equals("năm")) {
                    sNam = "lăm";
                }
                if (c == '1') {
                    strReturn = (strReturn + " mười " + sNam);
                } else {
                    strReturn = (d == '1') ? (strReturn + " " + chu[toInt(c)] + " mươi mốt") : (strReturn + " "
                            + chu[toInt(c)] + " mươi " + sNam);
                }
            } else if ((t != '0') && (c == '0') && (d == '0')) {
                strReturn = chu[toInt(t)] + " trăm";
            } else if ((t != '0') && (c == '0') && (d != '0')) {
                strReturn = chu[toInt(t)] + " trăm lẻ " + chu[toInt(d)];
            } else if ((t != '0') && (c != '0') && (d == '0')) {
                strReturn = chu[toInt(t)] + " trăm";
                if (c == '1') {
                    strReturn = (strReturn + " mười");
                } else {
                    strReturn = (strReturn + " " + chu[toInt(c)] + " mươi");
                }
            }
        }
        return strReturn;
    }

    private static int toInt(char c) {
        return Integer.parseInt(String.valueOf(c));
    }

    public static String getNameOfGetPrefix(Method method) throws Exception {
        if (method.getName().length() > 3) {
            String prefix = method.getName().substring(0, 2);
            if (!prefix.equalsIgnoreCase("get")) {
                return null;
            }
            return method.getName().substring(3);
        }
        return null;
    }

    public static byte[] inputStreamToByteArray(DataInputStream dataInputStream, int bufferSize) throws Exception {
        byte[] rsArray = (byte[]) null;
        byte[] tmpArray = new byte[bufferSize];
        try {
            int n = dataInputStream.read(tmpArray);
            if (n > 0) {
                rsArray = new byte[n];
                System.arraycopy(tmpArray, 0, rsArray, 0, n);
            } else {
                throw new Exception("LOI CONVERT DU LIEU");
            }
        } catch (Exception e) {
            throw e;
        }
        return rsArray;
    }

    //    public static String convFormatCurrency(String amt, String curr) {
//        try {
//            if (curr.trim().equals("VND")) {
//                String _s = "";
//                if (amt.lastIndexOf(",") > 0) {
//                    _s = amt.substring(0, amt.lastIndexOf(",")).replace(".", "");
//                } else {
//                    _s = amt.replace(".", "");
//                }
//
//                return convFormatCurrencyToVni(_s);
//            } else if (curr.trim().equals("USD")) {
//                return convFormatCurrencyToForeign(amt);
//            }
//            return "";
//        } catch (Exception e) {
//            writeLog(Helper.class, e);
//            return "";
//        }
//
//    }
    public static String formatDate(Date date) {
        try {
            return formatDate(date, "dd/MM/yyyy");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(Date date, String formatString) {
        try {
            return (new SimpleDateFormat(formatString)).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date parseDate(Object dateValue) {
        Date date = new Date();
        try {
            if (dateValue == null) {
                return date;
            }
            if (dateValue instanceof String) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = dateFormat.parse(dateValue.toString());

            } else if (dateValue instanceof Number) {
                date = new Date(((Double) dateValue).longValue());
            }
        } catch (Exception e) {

        } finally {
            return date;
        }

    }

    public static Date parseDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    public static double parseDouble(String value) {
        if (value == null) {
            return 0;
        }
        try {
            double d = Double.parseDouble(value);
            return d;
        } catch (Exception e) {
            return 0;
        }
    }

    //
//    private static String convFormatCurrencyToVni(String value) {
//        try {
//            double d = Double.parseDouble(value);
//
//            DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,###");
//            return myFormatter.format(d);
//        } catch (Exception e) {
//            writeLog(Helper.class, e);
//            return "";
//        }
//    }
//
//    private static String convFormatCurrencyToForeign(String value) {
//        try {
//            value = value.replace(".", "");
//            value = value.replace(",", ".");
//
//            double d = Double.parseDouble(value);
//            DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,###.###");
//
//            return myFormatter.format(d);
//        } catch (Exception e) {
//            writeLog(Helper.class, e);
//            return "";
//        }
//    }
//    public static void main(String args[]) {
//        System.out.println(Common.isNotSignVietnameseValid("phuc"));
//    }
    public static int numByteOfString(String p_str) {
        try {
            if (isEmpty(p_str)) {
                return 0;
            }

            byte[] b = p_str.getBytes("UTF-8");
            return b.length;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * @param currentDate Ngay hien tai
     * @param period      Chu ky
     * @param addDay      Tu chon so ngay
     * @param addMonth    Tu chon so thang
     * @param addyear     Tu chon so nam
     * @return Ngay tiep theo
     */
    public static Date getNextDate(Date currentDate, int period, int addDay, int addMonth, int addyear) {
        Date sqlDate = null;
        if (currentDate == null) {
            sqlDate = new Date();
        } else {
            sqlDate = currentDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sqlDate);
        switch (period) {
            case 1:
                cal.add(Calendar.DAY_OF_YEAR, 1); // Hàng ngày
                break;
            case 2:
                cal.add(Calendar.WEEK_OF_YEAR, 1);  //Hàng tuần
                break;
            case 3:
                cal.add(Calendar.WEEK_OF_YEAR, 2); //Hai tuần một lần
                break;
            case 4:
                cal.add(Calendar.MONTH, 1); //Hàng tháng
                break;
            case 5:
                cal.add(Calendar.MONTH, 2); //Hai tháng một lần
                break;
            case 6:
                cal.add(Calendar.MONTH, 3);//Hàng quý
                break;
            case 7:
                cal.add(Calendar.MONTH, 6);//Nửa năm một lần
                break;
            case 8:
                cal.add(Calendar.YEAR, 1);//Hàng năm
                break;
            case 9:
                cal.add(Calendar.DAY_OF_YEAR, addDay);  //Tự chọn
                cal.add(Calendar.MONTH, addMonth);
                cal.add(Calendar.YEAR, addyear);
                break;
            default:
                throw new Error("Invalid date");

        }
        Date sqlTommorow = new Date(cal.getTimeInMillis());
        System.out.println("next date:" + sqlTommorow);
        return sqlTommorow;
    }

    public static String escapeSQL(String p_arg) throws Exception {
        if ((p_arg == null) || (p_arg.length() == 0)) {
            return p_arg;
        }

        String l_arg = p_arg;
        l_arg = l_arg.replaceAll("_", "\\_");
        l_arg = l_arg.replaceAll("%", "\\%");
        return l_arg;
    }

    public static Double convertToMoney(String strVnd) throws Exception {
        try {
            if (Common.isEmpty(strVnd)) {
                return null;
            }

            String _strTmp = strVnd.replaceAll(",", "");

            if (_strTmp.indexOf(".") > 0) {
                _strTmp = _strTmp.substring(0, _strTmp.indexOf("."));
            }

            if (Common.isNumeric(_strTmp)) {
                return Double.valueOf(_strTmp);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static String CreateJsessionId() {
        String string = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        Random random = new Random();
        StringBuffer sb = new StringBuffer(9);
        for (int i = 0; i < 9; i++) {
            sb.append(string.charAt(random.nextInt(string.length())));
        }
        return sb.toString();
    }

    public static String CreateRandomOTP(int lenght) {
        String string = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer(9);
        for (int i = 0; i < lenght; i++) {
            if(i%3 == 0){
                sb.append(" ");
            }
            sb.append(string.charAt(random.nextInt(string.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("err:" + Common.isNotSignVietnameseValid("~!@#$%&*"));
        System.out.println("err:" + Common.formatCurrency(1.0921832E7, "vnd"));

//        String a = new String("Hello a");
//        String b = new String("Hello b");
//        String c= SerializationUtils.clone(a);
//        c="Hello c";
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);
//
    }


    public static boolean isMobileNumberValid(String mobileNumber) {
        if (mobileNumber.length() < 10 || mobileNumber.length() > 11) {
            return false;
        }
        String regexStr = "^[0-9]*$";
        return mobileNumber.matches(regexStr);

    }

    public static String getMD5HashString(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytes = 0;
            do {
                bytes = fis.read(buffer, 0, bufferSize);
                if (bytes > 0) {
                    md.update(buffer, 0, bytes);
                }
            } while (bytes > 0);

            byte[] md5sum = md.digest();
            return bytesToHexString(md5sum);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSHA256HashString(String src) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] shasum = md.digest(src.getBytes(StandardCharsets.UTF_8));
            return bytesToHexString(shasum);
        } catch (Exception e) {
            return "";
        }
    }
    public static boolean compairHashString(String clearText, String hashedText){
        //us sha 256
        String encStr = getSHA256HashString(clearText);
        return encStr.equals(hashedText);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toHexString(0xFF & bytes[i]));
        }
        return sb.toString();
    }

    public String formatCurrencyOnReport(double value, String currency) {
        String curreny = currency == null ? "VND" : currency;
        return formatCurrency(value, curreny);
    }

}
