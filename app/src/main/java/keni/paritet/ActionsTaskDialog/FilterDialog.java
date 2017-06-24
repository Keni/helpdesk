package keni.paritet.ActionsTaskDialog;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.TasksFilter;
import keni.paritet.Config.Config;
import keni.paritet.Tools.MultiSelectionSpinner;
import keni.paritet.R;

/**
 * Created by Keni on 17.12.2016.
 */

public class FilterDialog
{
    public void filterDialog(final Fragment fragment)
    {
        final AlertDialog.Builder dialogFilter = new AlertDialog.Builder(fragment.getActivity());

        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        final Spinner spinnerPerformers = (Spinner) view.findViewById(R.id.spinnerPerformer);
        spinnerPerformers.setAdapter(arrayAdapter(fragment, Config.performers));

        final SearchableSpinner spinnerObjects = (SearchableSpinner) view.findViewById(R.id.spinnerObj);
        spinnerObjects.setAdapter(arrayAdapter(fragment, Config.objects));
        spinnerObjects.setTitle(String.valueOf(fragment.getText(R.string.selectObj)));
        spinnerObjects.setPositiveButton(String.valueOf(fragment.getText(R.string.buttonAccept)));

        final MultiSelectionSpinner spinnerPriority = (MultiSelectionSpinner) view.findViewById(R.id.spinnerPriority);
        spinnerPriority.setItems(Config.priority);
        spinnerPriority.setSelection(new int[]{0, 1, 2});

        final MultiSelectionSpinner spinnerStatus = (MultiSelectionSpinner) view.findViewById(R.id.spinnerStatus);
        spinnerStatus.setItems(Config.status);
        spinnerStatus.setSelection(new int[]{0, 1, 2, 3});

        final Spinner spinnerSort = (Spinner) view.findViewById(R.id.spinnerSort);
        List<String> listSort = Arrays.asList(fragment.getResources().getStringArray(R.array.sort));
        ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(fragment.getActivity(), R.layout.spinner_items, listSort);
        adapterSort.setDropDownViewResource(R.layout.spinner_dropdown_items);
        spinnerSort.setAdapter(adapterSort);

        final TextView inputDate0 = (TextView) view.findViewById(R.id.textViewDT0);
        final TextView inputDate1 = (TextView) view.findViewById(R.id.textViewDT1);

        inputDate0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(fragment.getActivity(), inputDate0);
            }
        });

        inputDate1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(fragment.getActivity(), inputDate1);
            }
        });

        dialogFilter.setView(view);
        dialogFilter.setTitle(R.string.filterTitle);
        dialogFilter.setIcon(R.drawable.ic_filter_outline_black_24dp);

        dialogFilter.setPositiveButton(R.string.buttonAccept, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String performer_id = null;
                String object_id = null;
                String dt0 = inputDate0.getText().toString();
                String dt1 = inputDate1.getText().toString();
                ArrayList<String> sortList = new ArrayList<>();

                if (spinnerPerformers.getSelectedItemPosition() == 0)
                    performer_id = "";
                else
                    for (int j = 0; j < Config.users.size(); j++)
                        if (Config.users.get(j).get(Config.TAG_USER).equals(spinnerPerformers.getSelectedItem().toString()))
                            performer_id = Config.users.get(j).get(Config.TAG_USER_ID);

                if (spinnerObjects.getSelectedItemPosition() == 0)
                    object_id = "";
                else
                    for (int j = 0; j < Config.objectsList.size(); j++)
                        if (Config.objectsList.get(j).get(Config.TAG_OBJECT_NAME).equals(spinnerObjects.getSelectedItem()))
                            object_id = Config.objectsList.get(j).get(Config.TAG_OBJECT_ID);

                String priority = spinnerPriority.getSelectedMap().keySet().toString().replace("[", "").replace("]", "").replace(" ", "");
                String status = spinnerStatus.getSelectedMap().keySet().toString().replace("[", "").replace("]", "").replace(" ", "");

                sortList.add("last_change_dt+DESC");
                sortList.add("last_change_dt");
                sortList.add("create_dt+DESC");
                sortList.add("create_dt");
                sortList.add("expire_dt+DESC");
                sortList.add("expire_dt");
                sortList.add("complete_dt+DESC");
                sortList.add("complete_dt");

                String sort = sortList.get(spinnerSort.getSelectedItemPosition());

                TasksFilter tf = new TasksFilter();
                tf.filterTasks(fragment, performer_id, object_id, priority, status, dt0, dt1, sort);
            }
        });

        dialogFilter.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        dialogFilter.show();
    }

    private ArrayAdapter arrayAdapter(Fragment fragment, ArrayList<String> list)
    {
        if (!list.get(0).equals(fragment.getText((R.string.all))))
            list.add(0, String.valueOf(fragment.getText(R.string.all)));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getActivity(), R.layout.spinner_items, list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_items);

        return adapter;
    }
}
