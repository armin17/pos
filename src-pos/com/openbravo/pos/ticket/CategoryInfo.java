//    Upsources POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Upsources POS.
//
//    Upsources POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Upsources POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Upsources POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import java.awt.image.*;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.loader.SerializerRead;

/**
 *
 * @author  Adrian
 * @version
 */
public class CategoryInfo implements IKeyed {

    private static final long serialVersionUID = 8612449444103L;
    private String m_sID;
    private String m_sName;
    private BufferedImage m_Image;
    private String m_sStartHour;
    private String m_sEndHour;

    /** Creates new CategoryInfo */
    public CategoryInfo(String id, String name, BufferedImage image) {
        m_sID = id;
        m_sName = name;
        m_Image = image;
        m_sStartHour = null;
        m_sEndHour = null;
    }

    public CategoryInfo(String id, String name, BufferedImage image, String sHour, String eHour) {
        m_sID = id;
        m_sName = name;
        m_Image = image;
        m_sStartHour = sHour;
        m_sEndHour = eHour;
    }

    public Object getKey() {
        return m_sID;
    }

    public void setID(String sID) {
        m_sID = sID;
    }

    public String getID() {
        return m_sID;
    }

    public String getName() {
        return m_sName;
    }

    public void setName(String sName) {
        m_sName = sName;
    }

    public BufferedImage getImage() {
        return m_Image;
    }

    public void setImage(BufferedImage img) {
        m_Image = img;
    }

    public String getStartHour() {
        return m_sStartHour;
    }

    public void setStartHour(String sHour) {
        m_sStartHour = sHour;
    }

    public String getEndHour() {
        return m_sEndHour;
    }

    public void setEndHour(String eHour) {
        m_sEndHour = eHour;
    }

    @Override
    public String toString() {
        return m_sName;
    }

    public static SerializerRead getSerializerRead() {
        return new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new CategoryInfo(
                    dr.getString(1),
                    dr.getString(2),
                    ImageUtils.readImage(dr.getBytes(3))
                );
            }
        };
    }

    public static SerializerRead getSerializerReadExt() {
        return new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new CategoryInfo(
                    dr.getString(1),
                    dr.getString(2),
                    ImageUtils.readImage(dr.getBytes(3)),
                    dr.getString(4),
                    dr.getString(5)
                );
            }
        };
    }
}
