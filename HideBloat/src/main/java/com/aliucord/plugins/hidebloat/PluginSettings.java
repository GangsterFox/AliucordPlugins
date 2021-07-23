package com.aliucord.plugins.hidebloat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.fragments.SettingsPage;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.aliucord.plugins.hidebloat.util.Util;
import com.aliucord.plugins.hidebloat.widgets.SwitchItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PluginSettings extends SettingsPage {

    private final SettingsAPI settingsAPI;

    public PluginSettings(SettingsAPI settingsAPI){
        this.settingsAPI = settingsAPI;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onViewBound(View view) {
        super.onViewBound(view);
        setActionBarTitle("Bloat");
        Context context = requireContext();

        Utils.threadPool.execute(() -> {

            List<SwitchItem> list = new ArrayList<>();

            for (BasePatcher patcher : Util.patches) {
                list.add(new SwitchItem(context, settingsAPI, patcher.key, patcher.viewName));
            }

            list.sort(Comparator.comparing(switchItem -> switchItem.viewName));

            Utils.mainThread.post(() -> {
                RecyclerView recyclerView = new RecyclerView(context);
                RecyclerAdapter adapter = new RecyclerAdapter(list);

                ShapeDrawable shape = new ShapeDrawable(new RectShape());
                shape.setTint(Color.TRANSPARENT);
                shape.setIntrinsicHeight(Utils.getDefaultPadding());

                DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
                decoration.setDrawable(shape);

                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(decoration);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

                addView(recyclerView);
            });
        });
    }
}
