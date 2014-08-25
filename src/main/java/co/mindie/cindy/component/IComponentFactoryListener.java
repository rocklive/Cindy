package co.mindie.cindy.component;

public interface IComponentFactoryListener {

	void onComponentCreated(Object componentInstance);

	void onComponentInitialized(Object componentInstance);

	void onComponentInitializationFailed(Object componentInstance, Exception e);

}