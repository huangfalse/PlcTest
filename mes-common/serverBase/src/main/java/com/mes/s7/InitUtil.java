package com.mes.s7;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mes.s7.device.PlcBitInfo;
import com.mes.s7.device.PlcBitObject;
import com.mes.s7.device.PlcParameterInfo;
import com.mes.s7.device.PlcParameterObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * InitUtil class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class InitUtil {
    /**
     * 初始化word
     */
    public static PlcParameterObject initword(String jsonFilePath) {
        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            bufferedReader.close();
            fileReader.close();

            JSONObject jsonfileobj = new JSONObject(content.toString());
            JSONArray jsonArray = jsonfileobj.getJSONArray("parameteInfor");
            PlcParameterObject plcParameterObject = new PlcParameterObject();
            //设置起始位地址
            plcParameterObject.setPlcAddressBegin(jsonfileobj.getStr("plcAddressBegin"));
            //设置地址长度
            plcParameterObject.setPlcAddressLength(Integer.valueOf(jsonfileobj.getStr("plcAddressLenght")));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject parameterObj = jsonArray.getJSONObject(i);
                //参数实例
                PlcParameterInfo plcParameterInfo = new PlcParameterInfo(jsonfileobj.getStr("plcAddressBegin"));
                String codeid = parameterObj.getStr("codeId");
                plcParameterInfo.setCodeId(codeid);
                plcParameterInfo.setAddressIndex(Integer.valueOf(parameterObj.getStr("addressIndex")));
                plcParameterInfo.setAddressLength(Integer.valueOf(parameterObj.getStr("addressLenght")));
                plcParameterInfo.setUnit(parameterObj.getStr("unit"));
                plcParameterObject.addPlcParameter(plcParameterInfo);
            }

            return plcParameterObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化bit
     */
    public static PlcBitObject initbit(String jsonFilePath) {
        PlcBitObject plcBitObject = new PlcBitObject();

        try {
            FileReader fileReader = new FileReader(jsonFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            bufferedReader.close();
            fileReader.close();

            JSONObject jsonfileobj = new JSONObject(content.toString());
            JSONArray jsonArray = jsonfileobj.getJSONArray("parameteInfor");
            //设置起始位地址
            plcBitObject.setPlcAddressBegin(jsonfileobj.getStr("plcAddressBegin"));
            //设置地址长度
            plcBitObject.setPlcAddressLength(Integer.valueOf(jsonfileobj.getStr("plcAddressLenght")));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject parameterObj = jsonArray.getJSONObject(i);
                //参数实例
                PlcBitInfo plcBitInfo = new PlcBitInfo(jsonfileobj.getStr("plcAddressBegin"));
                String codeid = parameterObj.getStr("codeId");
                plcBitInfo.setCodeId(codeid);
                plcBitInfo.setAddressIndex(Integer.valueOf(parameterObj.getStr("addressIndex")));

                plcBitObject.addPlcBit(plcBitInfo);
            }
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plcBitObject;
    }
}
