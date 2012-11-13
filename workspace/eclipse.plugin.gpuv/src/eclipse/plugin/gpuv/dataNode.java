package eclipse.plugin.gpuv;

public class dataNode {
	private String name;
	private String option;
	private String argType;
	private int argNum;
	private String type;
	private boolean multiple;
	private String description;
	public dataNode(String name, String option, String argType, int argNum, String type, boolean multiple, String description){
		this.setName(name);
		this.setOption(option);
		this.setArgType(argType);
		this.setArgNum(argNum);
		this.setType(type);
		this.setMultiple(multiple);
		this.setDescription(description);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getArgType() {
		return argType;
	}
	public void setArgType(String argType) {
		this.argType = argType;
	}
	public int getArgNum() {
		return argNum;
	}
	public void setArgNum(int argNum) {
		this.argNum = argNum;
	}
	
}