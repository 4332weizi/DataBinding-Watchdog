# DataBinding-Watchdog
---

watchdog [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/watchdog/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/watchdog/_latestVersion)

compiler [ ![Download](https://api.bintray.com/packages/4332weizi/DataBinding-Watchdog/compiler/images/download.svg) ](https://bintray.com/4332weizi/DataBinding-Watchdog/compiler/_latestVersion)

Interface generator for Observable Field in android DataBinding.

when you write  
```java
@WatchThis  
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
when `onLoginSuccess` changed, method `onLoginSuccess(observableField, fieldId)` will be called.

Download
===
```
dependencies {
    compile 'net.funol.databinding.watchdog:watchdog:1.0.0'
    annotationProcessor 'net.funol.databinding.watchdog:compiler:1.0.0'
}
```