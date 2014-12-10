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

public class FunctionHandler {
	private List<FunctionInfo> infos = new ArrayList<FunctionInfo>();

	private Map<String, String> funinfo = new HashMap<String, String>();

	public void loadInfo(String filePath) throws IOException {
		FileReader fr = new FileReader(new File(filePath));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(" ");
			int index = 0;
			while (!tmp[index].startsWith("com"))
				index++;
			FunctionInfo info = new FunctionInfo(tmp[index],
					Integer.parseInt(tmp[index + 1]),
					Integer.parseInt(tmp[index + 2]));
			infos.add(info);
		}
		br.close();
	}

	public void constructFunMap(String filePath) throws FileNotFoundException,
			IOException {
		FileReader fr = new FileReader(new File(filePath));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(";");
			funinfo.put(tmp[0], tmp[1]);
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

			List<String> refbyList = new ArrayList<String>();
			String refbyTmp = tmp[1].substring(1, tmp[1].length() - 1);
			if (refbyTmp.length() > 0) {
				String[] refbyListTmp = refbyTmp.split("#");
				for (String s : refbyListTmp) {
					refbyList.add(s);
				}
			}

			List<String> refList = new ArrayList<String>();
			String refTmp = tmp[2].substring(1, tmp[2].length() - 1);
			if (refTmp.length() > 0) {
				String[] refListTmp = refTmp.split("#");
				for (String s : refListTmp) {
					refList.add(s);
				}
			}

			FunctionInfo info = new FunctionInfo(id, refbyList, refList);
			infos.add(info);
		}
		br.close();
	}

	public String getReferenceCountInfo() {
		String result = "";
		Collections.sort(infos, new Comparator<FunctionInfo>() {

			@Override
			public int compare(FunctionInfo o1, FunctionInfo o2) {
				return Integer.compare(o2.getReferenceCount(),
						o1.getReferenceCount());
			}
		});
		result = getTopTenFunctionName();
		return result;
	}

	public String getReferencedbyCountInfo() {
		String result = "";
		Collections.sort(infos, new Comparator<FunctionInfo>() {

			@Override
			public int compare(FunctionInfo o1, FunctionInfo o2) {
				return Integer.compare(o2.getReferencedbyCount(),
						o1.getReferencedbyCount());
			}
		});
		result = getTopTenFunctionName();
		return result;
	}

	public String getTopTenFunctionName() {
		String result = "";
		int count = 0;
		for (FunctionInfo info : infos) {
			if (count <= 10) {
				result += info.getName() + "\n";
				count++;
			} else
				break;
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		FunctionHandler fh = new FunctionHandler();
		fh.constructFunMap("/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/函数信息散列表.txt");
		fh.constructFunsInfos("/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/函数信息.txt");

		System.out.println();
	}

	public List<FunctionInfo> getInfos() {
		return this.infos;
	}

	public Map<String, String> getFunInfo() {
		return this.funinfo;
	}

	public PieDataset createReferencedbyDataSet() {
		DefaultPieDataset result = new DefaultPieDataset();
		Collections.sort(infos, new Comparator<FunctionInfo>() {

			@Override
			public int compare(FunctionInfo o1, FunctionInfo o2) {
				return Integer.compare(o2.getReferencedbyID().size(), o1
						.getReferencedbyID().size());
			}
		});
		int count = 0;
		for (FunctionInfo info : infos) {
			count += info.getReferencedbyID().size();
		}
		int index = 0, tmpCount = 0;
		for (FunctionInfo info : infos) {
			if (index < 10) {
				result.setValue(funinfo.get(info.getID()), info
						.getReferencedbyID().size());
				tmpCount += info.getReferencedbyID().size();
				index++;
			} else
				break;
		}
		result.setValue("others", count - tmpCount);
		return result;
	}

	public PieDataset createReferenceDataSet() {
		DefaultPieDataset result = new DefaultPieDataset();
		Collections.sort(infos, new Comparator<FunctionInfo>() {

			@Override
			public int compare(FunctionInfo o1, FunctionInfo o2) {
				return Integer.compare(o2.getReferenceID().size(), o1
						.getReferenceID().size());
			}
		});
		int count = 0;
		for (FunctionInfo info : infos) {
			count += info.getReferenceID().size();
		}
		int index = 0, tmpCount = 0;
		for (FunctionInfo info : infos) {
			if (index < 10) {
				result.setValue(funinfo.get(info.getID()), info
						.getReferenceID().size());
				tmpCount += info.getReferenceID().size();
				index++;
			} else
				break;
		}
		result.setValue("others", count - tmpCount);
		return result;
	}
}
