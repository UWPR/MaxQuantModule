package org.labkey.mq.query;

import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.DataColumn;
import org.labkey.api.data.DisplayColumn;
import org.labkey.api.data.DisplayColumnFactory;
import org.labkey.api.data.RenderContext;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.QueryAction;
import org.labkey.api.query.QueryService;
import org.labkey.api.view.ActionURL;
import org.labkey.mq.MqSchema;

import java.util.Set;

/**
 * Created by vsharma on 4/22/2016.
 */
public class QueryLinkDisplayColumnFactory implements DisplayColumnFactory
{

    private final String _tableName;
    private final String fkColumnName;
    private final String _valueColumnName;

    public QueryLinkDisplayColumnFactory(String tableName, String valueColumn, String fkColumnName)
    {
        _tableName = tableName;
        _valueColumnName = valueColumn;
        this.fkColumnName = fkColumnName;
    }

    @Override
    public DisplayColumn createRenderer(ColumnInfo colInfo)
    {
        return new DataColumn(colInfo)
        {
            @Override
            public void addQueryFieldKeys(Set<FieldKey> keys)
            {
                super.addQueryFieldKeys(keys);
                keys.add(FieldKey.fromParts(_valueColumnName));// Make sure this column is always in the result set.
            }

            @Override
            public String renderURL(RenderContext ctx)
            {
                String fkValue = String.valueOf(ctx.get(_valueColumnName));
                ActionURL url = QueryService.get().urlDefault(ctx.getContainer(), QueryAction.executeQuery, MqSchema.NAME, _tableName);
                url.addParameter("query." + fkColumnName + "~eq", fkValue);
                return url.getLocalURIString();
            }
        };
    }
}
