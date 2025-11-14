
package jge3.game.world;

import java.util.ArrayList;
import java.util.List;

import jge3.game.world.objects.Danger;
import jge3.game.world.objects.Platform;
import jge3.game.world.objects.Text;
import jge3.math.Vector3f;
import jge3.math.Vector4f;
import jge3.src3d.object.lights.AmbientLight;
import jge3.src3d.object.lights.DirectionalLight;
import jge3.utils.FileUtils;

public class WorldLoader {
	
	private static class DataContainer {
		public String name;
		public String[] sdata;
		public float[] fdata;
		public DataContainer(String name, List<String> data) {
			this.name = name;
			sdata = new String[data.size()];
			fdata = new float[data.size()];
			for (int i=0;i<sdata.length;i++) {
				sdata[i] = data.get(i);
				try {
					fdata[i] = Float.parseFloat(data.get(i));
				} catch (Exception e) {
					fdata[i] = 0f;
				}
			}
		}
	}
	
	public static void loadWorld(String filePath, GameWorld world) {
		List<DataContainer> data = parseFile(FileUtils.loadFile(filePath));
		for (DataContainer container : data) {
			String[] sd = container.sdata;
			float[] fd = container.fdata;
			switch (container.name) {
			case "spawnpoint":
				world.setSpawnpoint(new Vector3f(fd[0], fd[1], fd[2]));
				break;
			case "milk":
				world.setMilkPos(new Vector3f(fd[0], fd[1], fd[2]));
				break;
			case "goal":
				world.setGoal(new Vector3f(fd[0], fd[1], fd[2]));
				break;
			case "death_height":
				world.setDeathHeight(fd[0]);
				break;
			case "platform":
				world.addObject(new Platform(fd[0], fd[1], fd[2], fd[3], fd[4], fd[5]));
				break;
			case "danger":
				world.addObject(new Danger(fd[0], fd[1], fd[2], fd[3], fd[4], fd[5]));
				break;
			case "text":
				world.addObject(new Text(fd[0], fd[1], fd[2], sd[3], fd[4]));
				break;
			}
		}
		world.getWorld().addLight(new AmbientLight(new Vector4f(1f, 1f, 1f, 1f), 0.5f));
		world.getWorld().addLight(new DirectionalLight(new Vector4f(1f, 1f, 1f, 1f), 0.5f));
	}
	
	private static List<DataContainer> parseFile(String data) {
		String[] split = data.split("}");
		List<DataContainer> containers = new ArrayList<DataContainer>();
		for (int i=0;i<split.length;i++) {
			int start = split[i].indexOf('{');
			int dataSplit = split[i].indexOf(':');
			if (start < 0 || dataSplit < 0) {
				continue;
			}
			String name = split[i].substring(start+1, dataSplit);
			List<String> stringData = new ArrayList<String>();
			int last = dataSplit;
			for (int j=last+1;j<split[i].length();j++) {
				char c = split[i].charAt(j);
				if (c == ',') {
					stringData.add(split[i].substring(last+1, j));
					last = j;
				}
			}
			stringData.add(split[i].substring(last+1, split[i].length()));
			containers.add(new DataContainer(name, stringData));
		}
		return containers;
	}
	
	public static void saveWorld(String filePath, GameWorld world) {
		String data = "";
		
		data += saveData("spawnpoint", world.getSpawnpoint());
		data += saveData("milk", world.getMilkPos());
		data += saveData("goal", world.getGoal());
		data += saveData("death_height", new float[] {world.getDeathHeight()});
		
		Object[] objects = world.getObjects();
		for (int i=0;i<objects.length;i++) {
			Object obj = objects[i];
			if (obj instanceof Platform) {
				Platform platform = (Platform)obj;
				Vector3f pos = platform.getPosition();
				Vector3f size = platform.getSize();
				data += saveData("platform", new float[] {pos.x, pos.y, pos.z, size.x, size.y, size.z});
			} else if (obj instanceof Danger) {
				Danger danger = (Danger)obj;
				Vector3f pos = danger.getPosition();
				Vector3f size = danger.getSize();
				data += saveData("danger", new float[] {pos.x, pos.y, pos.z, size.x, size.y, size.z});
			} else if (obj instanceof Text) {
				Text text = (Text)obj;
				Vector3f pos = text.getPosition();
				if (text.getText().length()>0) {
					data += saveData("text", new String[] {pos.x+"", pos.y+"", pos.z+"", text.getText(), text.getSize()+""});
				}
			}
		}
		
		FileUtils.createFile(filePath);
		FileUtils.writeFile(filePath, data);
	}
	
	private static String saveData(String name, Vector3f vec3Data) {
		return"{"+name+":"+vec3Data.x+","+vec3Data.y+","+vec3Data.z+"}\n";
	}
	
	private static String saveData(String name, String[] stringData) {
		String data = "{"+name+":"+stringData[0];
		for (int i=1;i<stringData.length;i++) {
			data += ","+stringData[i];
		}
		data += "}\n";
		return data;
	}
	
	private static String saveData(String name, float[] floatData) {
		String data = "{"+name+":"+floatData[0];
		for (int i=1;i<floatData.length;i++) {
			data += ","+floatData[i];
		}
		data += "}\n";
		return data;
	}
	
	@SuppressWarnings("unused")
	private static String saveData(String name, java.lang.Object[] allData) {
		String data = "{"+name+":"+allData[0].toString();
		for (int i=1;i<allData.length;i++) {
			data += ","+allData[i].toString();
		}
		data += "}\n";
		return data;
	}
	
}
