package eclipse.plugin.gpuv.views;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.PlatformUI;

public class ViewDisplayResultUpdate implements IViewDisplayResultUpdate {
	Set<String> items = new HashSet<String>();
	MainViewDisplayResults view;
	
	public ViewDisplayResultUpdate(){
		view =  (MainViewDisplayResults) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("eclipse.plugin.gpuv.gpuview");
	}
	
	public void addViewItems(String item){
		items.add(item);
	}
	
	public void printItems(){
		for(String item : items){
			view.updateView(item);
		}
	}
	
	public void clearItems(){
		while(!items.isEmpty()){
			view.clearView(0);
		}
	}

}
