/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 * AJAX web applications by using Java and jQuery
 * openAUSIAS is distributed under the MIT License (MIT)
 * Sources at https://github.com/rafaelaznar/openAUSIAS
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
package net.daw.dao.specific.implementation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.daw.bean.meta.MetaBeanGenImpl;
import net.daw.bean.specific.implementation.ProfesorBean;
import net.daw.dao.generic.implementation.TableDaoGenImpl;
import net.daw.data.specific.implementation.MysqlDataSpImpl;
import net.daw.helper.annotations.MethodMetaInformation;
import net.daw.helper.statics.AppConfigurationHelper;
import net.daw.helper.statics.ExceptionBooster;
import net.daw.helper.statics.FilterBeanHelper;
import net.daw.helper.statics.SqlBuilder;

/**
 *
 * @author a020864288e
 */
public class ProfesorDao extends TableDaoGenImpl<ProfesorBean> {

    public ProfesorDao(Connection pooledConnection) throws Exception {
        super(pooledConnection);
    }

    @Override
    public ProfesorBean get(ProfesorBean oProfesorBean, Integer expand) throws Exception {
        MysqlDataSpImpl oMysql = new MysqlDataSpImpl(oConnection);
        if (oProfesorBean.getId() > 0) {

            if (oMysql.existsOne(strSqlSelectDataOrigin, oProfesorBean.getId())) {
                oProfesorBean.setNombre(oMysql.getOne(strSqlSelectDataOrigin, "nombre", oProfesorBean.getId()));
                oProfesorBean.setEstado(oMysql.getOne(strSqlSelectDataOrigin, "estado", oProfesorBean.getId()));
         }
        }
        try {

            return oProfesorBean;
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ":get ERROR: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<ProfesorBean> getAll(ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {

        MysqlDataSpImpl oMysql = new MysqlDataSpImpl(oConnection);

        ArrayList<ProfesorBean> alProfesorBean = new ArrayList<>();
        strSqlSelectDataOrigin += SqlBuilder.buildSqlWhere(alFilter);
        strSqlSelectDataOrigin += SqlBuilder.buildSqlOrder(hmOrder);
        try {
            ResultSet result = oMysql.getAllSql(strSqlSelectDataOrigin);
            if (result != null) {
                while (result.next()) {
                    ProfesorBean oProfesorBean = new ProfesorBean();
                    oProfesorBean.setId(result.getInt("id"));
                    oProfesorBean.setNombre(result.getString("nombre"));
                    oProfesorBean.setEstado(result.getString("estado"));
                    alProfesorBean.add(oProfesorBean);
                }
            }

        } catch (Exception ex) {
            throw new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage());
        }

        return alProfesorBean;
    }

    @Override
    public int getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception {

        MysqlDataSpImpl oMysql = new MysqlDataSpImpl(oConnection);
        strSqlSelectDataOrigin += SqlBuilder.buildSqlWhere(alFilter);
        int cont = 0;

        try {
            cont = oMysql.getCount(strSqlSelectDataOrigin);

        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".getCount: Error: " + e.getMessage());
        }

        return cont;
    }

    @Override
    public int getPages(int intRegsPerPag, ArrayList<FilterBeanHelper> alFilter) throws Exception {

        MysqlDataSpImpl oMysql = new MysqlDataSpImpl(oConnection);
        strSqlSelectDataOrigin += SqlBuilder.buildSqlWhere(alFilter);
        int cont = 0;

        try {
            cont = oMysql.getPages(strSqlSelectDataOrigin, intRegsPerPag);

        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".getPages: Error: " + e.getMessage());
        }

        return cont;
    }

    @Override
    public ArrayList<ProfesorBean> getPage(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {

        MysqlDataSpImpl oMysql = new MysqlDataSpImpl(oConnection);
        strSqlSelectDataOrigin += SqlBuilder.buildSqlWhere(alFilter);
        strSqlSelectDataOrigin += SqlBuilder.buildSqlOrder(hmOrder);
        ArrayList<ProfesorBean> alProfesorBean = new ArrayList<>();
        try {
            ResultSet result = oMysql.getPage(strSqlSelectDataOrigin, intRegsPerPag, intPage);

            while (result.next()) {
                ProfesorBean oProfesorBean = new ProfesorBean();
                oProfesorBean.setId(result.getInt("id"));
                oProfesorBean.setNombre(result.getString("nombre"));
                oProfesorBean.setEstado(result.getString("estado"));
                alProfesorBean.add(oProfesorBean);
            }
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".getPage: Error: " + e.getMessage());
        }

        return alProfesorBean;
    }

    @Override
    public int remove(ProfesorBean oProfesorBean) throws Exception {

        int result = 0;
        try {
            result = oMysql.removeOne(oProfesorBean.getId(), strTableOrigin);
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".remove: Error: " + e.getMessage());
        }
        return result;
    }

    @Override
    public ProfesorBean set(ProfesorBean oProfesorBean) throws Exception {

        try {
            if (oProfesorBean.getId() == 0) {
                oProfesorBean.setId(oMysql.insertOne(strTableOrigin));
            } 
                oMysql.updateOne(oProfesorBean.getId(), strTableOrigin, "nombre", oProfesorBean.getNombre());
                oMysql.updateOne(oProfesorBean.getId(), strTableOrigin, "estado", oProfesorBean.getEstado());
            
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + ".set: Error: " + e.getMessage());
        }
        return oProfesorBean;
    }
    
   public ArrayList<MetaBeanGenImpl> getmetainformation() throws Exception {
        ArrayList<MetaBeanGenImpl> alVector = null;
        try {
            Class oProfesorBeanClass = ProfesorBean.class;
            alVector = new ArrayList<>();
            for (Field field : oProfesorBeanClass.getDeclaredFields()) {
                Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
                for (Integer i = 0; i < fieldAnnotations.length; i++) {
                    if (fieldAnnotations[i].annotationType().equals(MethodMetaInformation.class)) {
                        MethodMetaInformation fieldAnnotation = (MethodMetaInformation) fieldAnnotations[i];
                        MetaBeanGenImpl oMeta = new MetaBeanGenImpl();
                        oMeta.setName(field.getName());
                        oMeta.setDefaultValue(fieldAnnotation.DefaultValue());
                        oMeta.setDescription(fieldAnnotation.Description());
                        oMeta.setIsId(fieldAnnotation.IsId());
                        oMeta.setIsObjForeignKey(fieldAnnotation.IsObjForeignKey());
                        oMeta.setMaxDecimal(fieldAnnotation.MaxDecimal());
                        oMeta.setMaxInteger(fieldAnnotation.MaxInteger());
                        oMeta.setMaxLength(fieldAnnotation.MaxLength());
                        oMeta.setMinLength(fieldAnnotation.MinLength());
                        oMeta.setMyIdName(fieldAnnotation.MyIdName());
                        oMeta.setReferencesTable(fieldAnnotation.ReferencesTable());
                        oMeta.setIsForeignKeyDescriptor(fieldAnnotation.IsForeignKeyDescriptor());
                        oMeta.setShortName(fieldAnnotation.ShortName());
                        oMeta.setType(fieldAnnotation.Type());
                        oMeta.setUltraShortName(fieldAnnotation.UltraShortName());
                        alVector.add(oMeta);
                    }
                }
            }
        } catch (Exception ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getmetainformation ERROR: " + ex.getMessage()));
        }
        return alVector;
    }
}
