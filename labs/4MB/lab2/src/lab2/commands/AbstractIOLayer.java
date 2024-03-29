package lab2.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс для работы с файловой системой
 */
public abstract class AbstractIOLayer {

	private File _currentDirectory;

	public AbstractIOLayer() {
		_currentDirectory = getRoot(new File(System.getProperty("user.dir")));
	}

	public File getCurrentDirectory() {
		return _currentDirectory;
	}

	public abstract boolean requestRemoving(String fileName);

	// ------------------------------------ Commands ---------------------------

	public String ls() {
		StringBuffer result = new StringBuffer();
		File[] files = _currentDirectory.listFiles();
		File file;
		// Запись папок
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			if (file.isDirectory()) {
				result.append("\t[D]\t" + file.getName()
						+ System.getProperty("line.separator"));
			}
		}
		// Запись файлов
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			if (!file.isDirectory()) {
				result.append("\t[f]\t" + file.getName()
						+ System.getProperty("line.separator"));
			}
		}

		int sepLength = System.getProperty("line.separator").length();
		if (result.length() > sepLength) {
			result.setLength(result.length() - sepLength);
		}
		return result.toString();
	}

	public String rm(String fileName) throws IOException {
		File file = new File(getAbsFile(fileName));
		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}
		if (file.isDirectory()) {
			if ((file.listFiles().length != 0) && (!requestRemoving(fileName))) {
				return "File removing cancelled";

			}

			String absCurDir = _currentDirectory.getPath();
			String removingFPath = file.getPath();
			if (absCurDir.startsWith(removingFPath)) {
				_currentDirectory = file.getParentFile();
			}
		}
		delete(file);
		return "File '" + fileName + "' removed.";
	}

	public String rename(String fileName, String newName) throws IOException {
		File file = new File(getAbsFile(fileName));
		File newFile = null;

		// Проверка существования файла
		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}

		// Проверка имени файла
		String os = System.getProperty("os.name");
		String invalidChars;
		if (os.toLowerCase().contains("windows")) {
			invalidChars = "\\/:*?\"<>|";
		} else if (os.toLowerCase().contains("mac")) {
			invalidChars = "/:";
		} else {
			invalidChars = "/";
		}
		for (char c : invalidChars.toCharArray()) {
			if (newName.contains(String.valueOf(c))) {
				throw new IllegalArgumentException("Invalid new file name");
			}
		}

		// Процерка на переименование родительской директории текущей
		// директории (какого либо уровня)
		if (file.isDirectory()) {
			String absCurDir = _currentDirectory.getPath();
			String renamingFPath = file.getPath();
			if (absCurDir.startsWith(renamingFPath)) {
				_currentDirectory = new File(file.getParent()
						+ File.separatorChar
						+ newName
						+ _currentDirectory.getAbsolutePath().substring(
								renamingFPath.length()));
			}
		}

		newFile = new File(file.getParent() + File.separatorChar + newName);
		file.renameTo(newFile);
		return "File '" + fileName + "' renamed to '" + newName + "'.";
	}

	public void cd(String fileName) throws IOException {
		File file = new File(getAbsFile(fileName));
		if (!file.exists()) {
			throw new IllegalArgumentException("File not found");
		}
		_currentDirectory = file;
	}

	// ----------------------------------- !Commands ---------------------------

	/**
	 * Рекурсивное удаление
	 * 
	 * @param file
	 *            Файл(каталог либо одиночный файл)
	 */
	private void delete(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				delete(f);
			}
		}
		file.delete();
	}

	/**
	 * Возвращает абсолютный файл
	 * 
	 * @param fileName
	 *            Имя файла относительно текущего каталога, либо абсолютный путь
	 *            к файлу
	 * @return Абсолютный путь к файлу
	 * @throws IOException
	 */
	private String getAbsFile(String fileName) throws IOException {
		List<String> filePathLexems = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();

		// check on a relative path
		boolean filePathIsAbs = false;

		for (File root : File.listRoots()) {
			if (fileName.startsWith(root.getPath())) {
				filePathIsAbs = true;
				break;
			}
		}

		// patching path, if it is relative
		if (!filePathIsAbs)
			fileName = _currentDirectory.getCanonicalPath()
					+ File.separatorChar + fileName;

		File file = new File(fileName);

		// spliting path by lexems
		String os = System.getProperty("os.name");
		String pathSeparator = null;
		if (os.toLowerCase().startsWith("windows")) {
			pathSeparator = File.separator + File.separator;
		} else if ("linux".equals(os.toLowerCase())) {
			pathSeparator = File.separator;
		}

		for (String filePathLexem : file.getPath().split(pathSeparator, 0)) {
			if (filePathLexem.equals("..")) {
				if (filePathLexems.size() > 0) { // на всякий случай
					filePathLexems.remove(filePathLexems.size() - 1);
				}
			} else if (!filePathLexem.equals(".")) {
				filePathLexems.add(filePathLexem);
			}

		}

		// constructing new resolved path
		for (int i = 0; i < filePathLexems.size(); i++) {
			sb.append(filePathLexems.get(i) + File.separatorChar);
		}

		if (filePathLexems.size() == 0) {
			sb.append(file.getPath().substring(0,
					file.getPath().indexOf(File.separator))
					+ File.separatorChar);
		}
		return new File(sb.toString()).getAbsolutePath();
	}

	/**
	 * Получение корневой директории файла
	 * 
	 * @param file
	 *            Файл
	 * @return Корневая директория файла
	 */
	private File getRoot(File file) {
		while (true) {
			file = file.getParentFile();
			for (File root : File.listRoots()) {
				if (root.equals(file)) {
					return file;
				}
			}
		}

	}

}
