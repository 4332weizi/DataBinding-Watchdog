# DataBinding-Watchdog
---

watchdog [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/watchdog/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/watchdog/_latestVersion)

compiler [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/compiler/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/compiler/_latestVersion)

Interface generator for Observable Field in android DataBinding.

when you write  
```java
 public class MainViewModel {
    @WatchThis
    public final BaseObservable onLoginSuccess = new BaseObservable();
}
```
an interface will be generate for OnPropertyChangeCallback of Field `onLoginSuccess`.
```java
public interface IMainViewModelCallbacks {
  @NotifyThis
  void onLoginSuccess(BaseObservable observableField, int fieldId);
}
```
implement `IMainViewModelCallbacks` and bind it to `onLoginSuccess` property change callback.
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
    public void onLoginSuccess(BaseObservable observableField, int fieldId) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

}
```
when `onLoginSuccess` property changed, method `onLoginSuccess(observableField, fieldId)` will be called.

Download
===
```
dependencies {
    compile 'net.funol.databinding.watchdog:watchdog:1.0.0'
    annotationProcessor 'net.funol.databinding.watchdog:compiler:1.0.0'
}
```