/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.client.ui.panels.reference;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.client.ui.util.VariableListUtil;
import com.webreach.mirth.model.Rule;
import com.webreach.mirth.model.Step;
import java.util.LinkedHashSet;

public class VariableReferenceTable extends ReferenceTable
{
    private Object[] tooltip;

    private ArrayList<ReferenceListItem> _listItems;

    private String headerName = "blank";

    public VariableReferenceTable()
    {
        super();
        makeTable(null, null);
    }

    public VariableReferenceTable(String headerName, Object[] data)
    {
        super();
        makeTable(headerName, data, null);
    }

    public VariableReferenceTable(String headerName, Object[] data, Object[] tooltip)
    {
        super();
        makeTable(headerName, data, tooltip);
    }

    public VariableReferenceTable(String headerName, ArrayList<ReferenceListItem> listItems)
    {
        this._listItems = listItems;
        makeTable(headerName, listItems);
    }

    private void makeTable(String headerName, ArrayList<ReferenceListItem> listItems)
    {
        if (listItems == null)
            return;
        Object[] tooltips = new String[listItems.size()];
        Object[] names = new String[listItems.size()];
        Iterator<ReferenceListItem> listItemIterator = listItems.iterator();
        int i = 0;
        while (listItemIterator.hasNext())
        {
            ReferenceListItem listItem = listItemIterator.next();
            names[i] = listItem.getName();
            tooltips[i] = listItem.getTooltip();
            i++;
        }
        makeTable(headerName, names, tooltips);
    }

    private void makeTable(String headerName, Object[] data, Object[] tooltip)
    {
        if (data == null)
            return;

        this.headerName = headerName;
        this.tooltip = tooltip;

        Object[][] d = new String[data.length][2];
        for (int i = 0; i < data.length; i++)
        {
            d[i][0] = data[i];
            d[i][1] = null;
        }

        this.setModel(new DefaultTableModel(d, new Object[] { headerName })
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        });

        this.getColumnExt(headerName).setPreferredWidth(80);
        this.getColumnExt(headerName).setHeaderRenderer(PlatformUI.CENTER_COLUMN_HEADER_RENDERER);

    }

    public String getToolTipText(MouseEvent event)
    {
        Point p = event.getPoint();
        int col = columnAtPoint(p);
        int row = rowAtPoint(p);
        if (col >= 0 && row >= 0 && tooltip != null)
        {
            Object o = getValueAt(row, col);
            if (o != null)
                return "<html><body style=\"width:150px\"><p>" + tooltip[row] + "</p></body></html>";
        }
        return null;
    }

    public void updateVariables(List<Rule> rules, List<Step> steps)
    {
        LinkedHashSet<String> variables = new LinkedHashSet<String>();
        
        if(rules != null)
            variables.addAll(VariableListUtil.getRuleVariables(rules));
        
        if(steps != null)
            variables.addAll(VariableListUtil.getStepVariables(steps));

        Object[][] d = new String[variables.toArray().length][2];
        for (int j = 0; j < variables.toArray().length; j++)
        {
            d[j][0] = variables.toArray()[j];
            d[j][1] = null;
        }

        this.setModel(new DefaultTableModel(d, new Object[] { headerName })
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        });

    }
}
