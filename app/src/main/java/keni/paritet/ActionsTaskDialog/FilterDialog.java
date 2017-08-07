package keni.paritet.ActionsTaskDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.TasksFilter;
import keni.paritet.Activitys.LoginActivity;
import keni.paritet.Activitys.MainActivity;
import keni.paritet.Config.Config;
import keni.paritet.R;
import keni.paritet.Tools.MultiSelectionSpinner;
import keni.paritet.Tools.SearchableSpinner;

/**
 * Created by Keni on 17.12.2016.
 */

/*************************************************************************************
Это жесть, фильтряция обращений, как она есть жестко и беспощадно, без сто грамм никак
*************************************************************************************/

public class FilterDialog
{
    public void filterDialog(final Fragment fragment)
    {
        final AlertDialog.Builder dialogFilter = new AlertDialog.Builder(fragment.getActivity());       // Создаем диалог

        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();                           // Создаем вью
        View view = inflater.inflate(R.layout.dialog_filter, null);                                     // Лайаут тела диалога
        RelativeLayout viewTitle = (RelativeLayout) fragment.getActivity().getLayoutInflater()          // Находим лайат тайтла
                .inflate(R.layout.dialog_custom_title, null);

        final Spinner spinnerPerformers = (Spinner) view.findViewById(R.id.spinnerPerformer);           // Находим спинер исполнителей
        spinnerPerformers.setAdapter(arrayAdapter(fragment, Config.performers));                        // Добавляем всех исполнителей с сервера

        final SearchableSpinner spinnerObjects = (SearchableSpinner)                                    // Находим СЕРЧ спинер обьектов
                view.findViewById(R.id.spinnerObj);
        spinnerObjects.setAdapter(arrayAdapter(fragment, Config.objects));                              // Добавляем все обьекты с сервера
        spinnerObjects.setTitle(String.valueOf(fragment.getText(R.string.selectObj)));                                       // Даем тайтл диалогу с поиском и выбором обьекта
        spinnerObjects.setPositiveButton(String.valueOf(fragment.getText(R.string.buttonAccept)));                                  // Название кнопки приминить

        final MultiSelectionSpinner spinnerPriority = (MultiSelectionSpinner)                           // Находим МУЛЬТИ спинер приоритетов обращений
                view.findViewById(R.id.spinnerPriority);
        spinnerPriority.setItems(Config.priority);                                                      // Добавляем список приоритетов загруженных с сервера
        List<Integer> priorityList = new ArrayList<>();                                                // Создаем Лист для приоритетов, которые по дефолту будут выбраны,
        priorityList.add(0);                                                                            // такой способ сделан для облегчения сохрарнения фильтра
        priorityList.add(1);
        priorityList.add(2);
        spinnerPriority.setSelectionInt(priorityList);                                                  // Приминяем выбранные элементы

        final MultiSelectionSpinner spinnerStatus = (MultiSelectionSpinner)                             // МУЛЬТИ спинер для статусов, обьснение как и выше
                view.findViewById(R.id.spinnerStatus);
        spinnerStatus.setItems(Config.status);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(0);
        statusList.add(1);
        statusList.add(2);
        statusList.add(3);
        spinnerStatus.setSelectionInt(statusList);

        final Spinner spinnerSort = (Spinner) view.findViewById(R.id.spinnerSort);                      // Спинер для сортировки
        List<String> listSort = Arrays.asList(fragment.getResources().getStringArray(R.array.sort));    // Список фильтров берется массив из стрингс.xml
        ArrayAdapter<String> adapterSort = new ArrayAdapter<>(fragment.getActivity(),
                R.layout.spinner_items, listSort);
        adapterSort.setDropDownViewResource(R.layout.spinner_dropdown_items);
        spinnerSort.setAdapter(adapterSort);                                                            // Применяем список фильтров к спинеру

        final TextView inputDate0 = (TextView) view.findViewById(R.id.textViewDT0);                     // Дата начала
        final TextView inputDate1 = (TextView) view.findViewById(R.id.textViewDT1);                     // Дата конец

        inputDate0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {                                                       // Запуск календаря и выборка даты
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

        dialogFilter.setView(view);                                                                     // Приминение всего вью
        dialogFilter.setCustomTitle(viewTitle);                                                         // Приминение кастом тайтл вью

        final TextView textViewTitle = (TextView) viewTitle.findViewById(R.id.textViewTitle);           // Установка надписи в тайтле
        textViewTitle.setText(R.string.filterTitle);

        final ImageView imageViewTitle = (ImageView) viewTitle.findViewById(R.id.imageViewTitle);       // Установка иконки в тайтле
        imageViewTitle.setImageResource(R.drawable.ic_filter_outline_black_24dp);

        SharedPreferences sharedPreferences = fragment.getActivity().                                   // Загрузка шаредпрефа для проверки на фильтр
                getSharedPreferences(Config.SHARED_PREF_FILTER, Context.MODE_PRIVATE);

        boolean filtered = sharedPreferences.getBoolean(Config.FILTERED, false);

        if (filtered)                                                                                   // Проверка фильтра, если тру, то подгружает сохраненые параметры, если фолс, то формирует дейфолтный вью
        {
            SharedPreferences prefs = fragment.getActivity().getPreferences(0);

            int prioritySize = prefs.getInt("spinnerPriority_size", 0);
            int statusSize = prefs.getInt("spinnerStatus_size", 0);

            priorityList.clear();
            statusList.clear();

            for (int i = 0; i < prioritySize; i++)
            {
                priorityList.add(prefs.getInt("Priority_" + i, 0));
            }

            for (int i = 0; i < statusSize; i++)
            {
                statusList.add(prefs.getInt("Status_" + i, 0));
            }

            spinnerPerformers.setSelection(prefs.getInt("spinnerPerformers", 0));
            spinnerPriority.setSelectionInt(priorityList);
            spinnerStatus.setSelectionInt(statusList);
            spinnerObjects.setSelection(prefs.getInt("spinnerObject", 0));
            inputDate0.setText(prefs.getString("DT0", ""));
            inputDate1.setText(prefs.getString("DT1", ""));
            spinnerSort.setSelection(prefs.getInt("spinnerSort", 0));
        }

        dialogFilter.setPositiveButton(R.string.buttonAccept, null);

        dialogFilter.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogFilter.create();                                               // Создание алерт диалоа

        dialog.show();

        final ImageButton imageViewClear = (ImageButton)                                                // Кнопка сброса фильтра, ее описание, иконка и действие
                viewTitle.findViewById(R.id.imageButtonSave);
        imageViewClear.setImageResource(R.drawable.ic_broom);
        imageViewClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sharedPreferences = fragment.getActivity().
                        getSharedPreferences(Config.SHARED_PREF_FILTER, Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                SharedPreferences prefs = fragment.getActivity().getPreferences(0);
                prefs.edit().clear().apply();

                fragment.getActivity().recreate();
                dialog.dismiss();

                Toast toast = Toast.makeText(fragment.getContext(),
                        R.string.filterClear, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()     // Приминение фильтра
        {
            @Override
            public void onClick(View v)
            {
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

                    SharedPreferences.Editor editor = fragment.getActivity().getPreferences(0).edit();
                    editor.putInt("spinnerPerformers", spinnerPerformers.getSelectedItemPosition());

                    editor.putInt("spinnerPriority_size", spinnerPriority.getSelectedInt().size());
                    for(int j = 0; j < spinnerPriority.getSelectedInt().size(); j++)
                    {
                        editor.remove("Priority_" + j);
                        editor.putInt("Priority_" + j, spinnerPriority.getSelectedInt().get(j));
                    }

                    editor.putInt("spinnerStatus_size", spinnerStatus.getSelectedInt().size());
                    for(int j = 0; j < spinnerStatus.getSelectedInt().size(); j++)
                    {
                        editor.remove("Status_" + j);
                        editor.putInt("Status_" + j, spinnerStatus.getSelectedInt().get(j));
                    }

                    editor.putInt("spinnerObject", spinnerObjects.getSelectedItemPosition());
                    editor.putString("DT0", dt0);
                    editor.putString("DT1", dt1);
                    editor.putInt("spinnerSort", spinnerSort.getSelectedItemPosition());
                    editor.apply();

                    SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences(Config.SHARED_PREF_FILTER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorFilter = sharedPreferences.edit();
                    editorFilter.putBoolean(Config.FILTERED, true);
                    editorFilter.putString(Config.FILTERED_PERFORMER, performer_id);
                    editorFilter.putString(Config.FILTERED_PRIORITY, priority);
                    editorFilter.putString(Config.FILTERED_OBJECT, object_id);
                    editorFilter.putString(Config.FILTERED_STATUS, status);
                    editorFilter.putString(Config.FILTERED_DT0, dt0);
                    editorFilter.putString(Config.FILTERED_DT1, dt1);
                    editorFilter.putString(Config.FILTERED_SORT, sort);

                    editorFilter.apply();

                    TasksFilter tf = new TasksFilter();
                    tf.filterTasks(fragment, performer_id, object_id, priority, status, dt0, dt1, sort);

                    dialog.dismiss();
                }
            }
        });
    }

    private ArrayAdapter arrayAdapter(Fragment fragment, ArrayList<String> list)                        // Формирование адаптера для спинеров, подрузгка с сервера
    {
        if (!list.get(0).equals(fragment.getText((R.string.all))))
            list.add(0, String.valueOf(fragment.getText(R.string.all)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getActivity(),
                R.layout.spinner_items, list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_items);

        return adapter;
    }
}
