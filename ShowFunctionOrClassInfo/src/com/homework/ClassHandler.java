package com.homework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class ClassHandler {
	private List<ClassInfo> infos = new ArrayList<ClassInfo>();

	private Map<String, String> map = new HashMap<String, String>();

	public void constructFunMap(String filePath) throws FileNotFoundException,
			IOException {
		FileReader fr = new FileReader(new File(filePath));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(";");
			map.put(tmp[0], tmp[1]);
		}
		br.close();
	}

	public void constructFunsInfos(String filePath) throws IOException {
		FileReader fr = new FileReader(new File(filePath));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(";");
			String id = tmp[0];

			List<String> relatedClassID = new ArrayList<String>();
			String refbyTmp = tmp[1].substring(1, tmp[1].length() - 1);
			if (refbyTmp.length() > 0) {
				String[] refbyListTmp = refbyTmp.split("#");
				for (String s : refbyListTmp) {
					relatedClassID.add(s);
				}
			}

			ClassInfo info = new ClassInfo(id, relatedClassID);
			infos.add(info);
		}
		br.close();
	}

	public PieDataset createRelatedIDDataSet() {
		DefaultPieDataset result = new DefaultPieDataset();
		Collections.sort(infos, new Comparator<ClassInfo>() {

			@Override
			public int compare(ClassInfo o1, ClassInfo o2) {
				return Integer.compare(o2.getRelatedClassID().size(), o1
						.getRelatedClassID().size());
			}
		});
		int count = 0;
		for (ClassInfo info : infos) {
			count += info.getRelatedClassID().size();
		}
		int index = 0, tmpCount = 0;
		for (ClassInfo info : infos) {
			if (index < 10) {
				result.setValue(map.get(info.getID()), info.getRelatedClassID()
						.size());
				tmpCount += info.getRelatedClassID().size();
				index++;
			} else
				break;
		}
		result.setValue("others", count - tmpCount);
		return result;
	}

	public List<ClassInfo> getClassInfos() {
		return this.infos;
	}

	public Map<String, String> getClassMap() {
		return this.map;
	}
}
