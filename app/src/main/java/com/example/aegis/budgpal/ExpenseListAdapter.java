package com.example.aegis.budgpal;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kyle on 4/19/2017.
 */

public class ExpenseListAdapter extends ArrayAdapter<FireExpense> {

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull FireExpense[] objects) {
        super(context, resource, objects);
    }

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull FireExpense[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public ExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }


    @Override
    public View getView(int code, View view, ViewGroup group){
        View horse;
        LayoutInflater hay = LayoutInflater.from(getContext());

        horse = hay.inflate(R.layout.expense_list_item, null);

        FireExpense bail = getItem(code);

        TextView name = (TextView)horse.findViewById(R.id.expenseName);
        TextView amount = (TextView)horse.findViewById(R.id.expenseAmount);
        name.setText(bail.getDescription());
        amount.setText(bail.getAmount() + "");

        return horse;
    }
}

/*package com.example.aegis.budgpal;
*/
/**
 * Created by Aegis on 4/21/17.
 */

/*public class ExpenseListAdapter {
}*/

