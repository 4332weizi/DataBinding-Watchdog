DataBinding-Watchdog
===

Interface generator and property change binding tool for observable field in android DataBinding.

* Generate callback interface for observable fields annotated with `@WatchThis`.
* Bind callback with `onPropertyChanged` event.

Download
---
```groovy
dependencies {
    compile 'io.auxo.databinding.watchdog:watchdog:2.0.0'
    annotationProcessor 'io.auxo.databinding.watchdog:compiler:2.0.0'
}
```

![Watchdog](image/watchdog.gif)

Before
---
```java
    public class ViewModel{
        public final BaseObservable a = new BaseObservable();
        public final ObservableInt b = new ObservableInt();
        public final ObservableField<String> c = new ObservableField<>();
    }

    public class DemoActivity extends Activity{
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.content);
            ViewModel viewModel = new ViewModel();
            binding.setVariable(BR.data, viewModel);

            initA(viewModel);
            initB(viewModel);
            initC(viewModel);
        }
        protected void initA(ViewModel viewModel){
            viewModel.a.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {

                }
            });
        }
        protected void initB(ViewModel viewModel){
            viewModel.b.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {

                }
            });
        }
        protected void initC(ViewModel viewModel){
            viewModel.c.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {

                }
            });
        }
    }
```

After
---
```java
    public class ViewModel {
        @WatchThis(method = "OnAChange")
        public final BaseObservable a = new BaseObservable();
        @WatchThis
        public final ObservableInt b = new ObservableInt();
        @WatchThis
        public final ObservableField<String> c = new ObservableField<>();
    }

    public class DemoActivity extends Activity implements IViewModelCallbacks {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.content);
            ViewModel viewModel = new ViewModel();
            binding.setVariable(BR.data, viewModel);

            Watchdog.watch(viewModel)
                    .addWatcher(this);
        }
        @Override
        public void OnAChange(BaseObservable observableField, int fieldId) {
        }
        @Override
        public void b(ObservableInt observableField, int fieldId) {
        }
        @Override
        public void c(ObservableField<String> observableField, int fieldId) {
        }
    }
```

License
-------

    Copyright (c) 2017 Victor Chiu

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
