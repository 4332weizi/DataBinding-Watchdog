# DataBinding-Watchdog
---

watchdog [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/watchdog/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/watchdog/_latestVersion)

compiler [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/compiler/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/compiler/_latestVersion)

Interface generator for Observable Field in android DataBinding.

when you write  
```java
 public class MainViewModel {
    @WatchThis(method = "onUserNameChanged")
    public final ObservableField<String> username = new ObservableField<>();
    @WatchThis(method = "onPasswordChanged")
    public final ObservableField<String> password = new ObservableField<>();
    @WatchThis
    public final BaseObservable onLoginSuccess = new BaseObservable();
}
```
an OnPropertyChangeCallback interface will be generate for fields annotated with `WatchThis`.
```java
public interface IMainViewModelCallbacks {
  @NotifyThis
  void onUserNameChanged(ObservableField<String> observableField, int fieldId);

  @NotifyThis
  void onPasswordChanged(ObservableField<String> observableField, int fieldId);

  @NotifyThis
  void onLoginSuccess(BaseObservable observableField, int fieldId);
}
```
implement `IMainViewModelCallbacks` and bind it to instance of `MainViewModel`.
```java
public class MainActivity extends AppCompatActivity implements IMainViewModelCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = new MainViewModel();
        binding.setWatchdog(viewModel);

        // bind
        Watchdog.newBuilder()
                .watch(viewModel)
                .notify(this)
                .build();
    }

    @Override
    public void onUserNameChanged(ObservableField<String> observableField, int fieldId) {
        showToast("用户名：" + observableField.get());
    }

    @Override
    public void onPasswordChanged(ObservableField<String> observableField, int fieldId) {
        showToast("密码：" + observableField.get());
    }

    @Override
    public void onLoginSuccess(BaseObservable observableField, int fieldId) {
        showToast("登录成功");
    }

    public void showToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
```
when being watched fields property changed, callback method will be called.

Download
===
```
dependencies {
    compile 'io.auxo.databinding.watchdog:watchdog:1.1.0'
    annotationProcessor 'io.auxo.databinding.watchdog:compiler:1.1.0'
}
```