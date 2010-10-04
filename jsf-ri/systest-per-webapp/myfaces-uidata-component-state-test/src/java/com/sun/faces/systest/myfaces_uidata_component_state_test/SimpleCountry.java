/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.systest.myfaces_uidata_component_state_test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author: lu4242 $)
 * @version $Revision: 692962 $ $Date: 2008-09-08 02:10:51 +0200 (Mo, 08 Sep 2008) $
 */
public class SimpleCountry implements Serializable
{
    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;
    private long _id;
	private String _name;
	private String _isoCode;
	private BigDecimal _size;
	private boolean _remove = false;
	private List _cities;
	private String mSortCitiesColumn;
	private boolean mIsSortCitiesAscending;
	
	public SimpleCountry(long id, String name, String isoCode, BigDecimal size, SimpleCity[] cities)
	{
		_id = id;
		_name = name;
		_isoCode = isoCode;
		_size = size;

		if (cities != null)
			_cities = new ArrayList(Arrays.asList(cities));
		else
			_cities = new ArrayList();
	}

	public long getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}

	public String getIsoCode()
	{
		return _isoCode;
	}

	public BigDecimal getSize()
	{
		return _size;
	}

	public List getCities()
	{
		if (mSortCitiesColumn != null)
		{
			Collections.sort(_cities, new Comparator()
			{
				public int compare(Object arg0, Object arg1)
				{
					SimpleCity lhs;
					SimpleCity rhs;
					if (isSortCitiesAscending())
					{
						lhs = (SimpleCity) arg0;
						rhs = (SimpleCity) arg1;
					}
					else
					{
						rhs = (SimpleCity) arg0;
						lhs = (SimpleCity) arg1;
					}
					String lhsName = lhs.getName();
					String rhsName = rhs.getName();
					if (lhsName != null)
					{
						if(rhsName != null)
						{
							return lhsName.compareToIgnoreCase(rhsName);
						}
						return -1;
					}
					else if (rhsName != null)
					{
						return 1;
					}
					return 0;
				}
			});
		}
		return _cities;
	}

	public void setId(long id)
	{
		_id = id;
	}

	public void setIsoCode(String isoCode)
	{
		_isoCode = isoCode;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setSize(BigDecimal size)
	{
		_size = size;
	}

	public boolean isRemove()
	{
		return _remove;
	}

	public void setRemove(boolean remove)
	{
		_remove = remove;
	}

	public String addCity()
	{
		getCities().add(new SimpleCity());
		return null;
	}

	public void deleteCity(ActionEvent ev)
	{
		UIData datatable = findParentHtmlDataTable(ev.getComponent());
		getCities().remove(datatable.getRowIndex() + datatable.getFirst());
	}

	public void setSortCitiesColumn(String columnName)
	{
		mSortCitiesColumn = columnName;
	}

	/**
	 * @return Returns the sortCitiesColumn.
	 */
	public String getSortCitiesColumn()
	{
		return mSortCitiesColumn;
	}

	public boolean isSortCitiesAscending()
	{
		return mIsSortCitiesAscending;
	}

	/**
	 * @param isSortCitiesAscending The isSortCitiesAscending to set.
	 */
	public void setSortCitiesAscending(boolean isSortCitiesAscending)
	{
		mIsSortCitiesAscending = isSortCitiesAscending;
	}

	/**
	 * @param component
	 * @return
	 */
	private HtmlDataTable findParentHtmlDataTable(UIComponent component)
	{
		if (component == null)
		{
			return null;
		}
		if (component instanceof HtmlDataTable)
		{
			return (HtmlDataTable) component;
		}
		return findParentHtmlDataTable(component.getParent());
	}
}
