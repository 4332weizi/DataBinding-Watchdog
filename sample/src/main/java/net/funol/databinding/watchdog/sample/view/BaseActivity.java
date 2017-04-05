package net.funol.databinding.watchdog.sample.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import net.funol.databinding.watchdog.sample.viewmodel.ViewModel;
import net.funol.databinding.watchdog.sample.viewmodel.watchdog.IViewModelCallbacks;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity implements IViewModelCallbacks {

    protected B mActivityDataBinding;
    protected Map<Integer, ? extends ViewModel> mDataBindingVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityDataBinding = DataBindingUtil.setContentView(this, getContentLayout());
        mDataBindingVariables = getDataBindingVariables(new HashMap<Integer, ViewModel>());

        for (int key : mDataBindingVariables.keySet()) {
            mActivityDataBinding.setVariable(key, mDataBindingVariables.get(key));
        }
    }

    protected abstract int getContentLayout();

    protected abstract Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables);

    protected B getActivityDataBinding() {
        return mActivityDataBinding;
    }

    protected void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showToast(ObservableField<String> observableField, int fieldId) {
        showToast(observableField.get());
    }
}
