package com.homework;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class TextEditorHelper {
	public static void openFile(String filePath) {
		final IFile inputFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(Path.fromOSString(filePath));
		if (inputFile != null) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					try {
						IEditorPart openEditor = IDE
								.openEditor(page, inputFile);
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void highlightFunction(String filePath, int line) {
		final IFile inputFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(Path.fromOSString(filePath));
		if (inputFile != null) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					try {
						IEditorPart openEditor = IDE
								.openEditor(page, inputFile);
						if (openEditor instanceof ITextEditor) {
							ITextEditor textEditor = (ITextEditor) openEditor;
							IDocument document = textEditor
									.getDocumentProvider().getDocument(
											textEditor.getEditorInput());
							try {
								textEditor.selectAndReveal(
										document.getLineOffset(line - 1),
										document.getLineLength(line - 1));
							} catch (org.eclipse.jface.text.BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}

	public static void main(String[] args) {
		String filePath = "/Users/berryjam/git/ShowFunctionOrClassinfo/ShowFunctionOrClassInfo/src/com/homework/TreeDemo.java";
		int line = 174;
		TextEditorHelper helper = new TextEditorHelper();
		helper.highlightFunction(filePath, line);
	}
}
