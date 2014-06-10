package de.vorb.swt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Class for showing message dialogs.
 * 
 * @author Paul Vorbach
 */
public class MessageDialog extends Dialog {
    /**
     * Text for this dialog's "OK" button. Change this value before creating
     * dialogs if you want to localize your application.
     */
    public static String buttonOKText = "OK";

    protected final Shell shell;

    /**
     * MessageDialog types.
     * 
     * @author Paul Vorbach
     */
    public static enum Type {
        /**
         * No special dialog type. No icon will be shown.
         */
        NONE,

        /**
         * Information message dialog. The system's default information icon
         * will be shown.
         */
        INFO,

        /**
         * Warning message dialog. The system's default warning icon will be
         * shown.
         */
        WARNING,

        /**
         * Error message dialog. The system's default error icon will be shown.
         */
        ERROR;
    }

    protected MessageDialog(Shell parent, int style, Type type, String title,
            String message) {
        super(parent, style);
        final Display display = getParent().getDisplay();

        // get the dialog icon
        final Image icon;
        switch (type) {
        case INFO:
            icon = display.getSystemImage(SWT.ICON_INFORMATION);
            break;
        case WARNING:
            icon = display.getSystemImage(SWT.ICON_WARNING);
            break;
        case ERROR:
            icon = display.getSystemImage(SWT.ICON_ERROR);
            break;
        default:
            icon = null;
        }

        shell = new Shell(getParent(), style);
        shell.setImage(icon);
        shell.setSize(340, 160);
        shell.setText(title);
        shell.setLayout(new FormLayout());

        Label lblIcon = null;
        if (icon != null) {
            lblIcon = new Label(shell, SWT.NONE);
            FormData fd_lblIcon = new FormData();
            fd_lblIcon.top = new FormAttachment(0, 10);
            fd_lblIcon.left = new FormAttachment(0, 10);
            lblIcon.setLayoutData(fd_lblIcon);
            lblIcon.setImage(icon);
            lblIcon.setSize(new Point(96, 96));
        }

        Label lblMessage = new Label(shell, SWT.WRAP);
        FormData fd_lblMessage = new FormData();
        fd_lblMessage.top = new FormAttachment(0, 15);
        if (icon != null) {
            fd_lblMessage.left = new FormAttachment(lblIcon, 10);
            fd_lblMessage.right = new FormAttachment(100, -10);
        } else {
            fd_lblMessage.left = new FormAttachment(0, 15);
            fd_lblMessage.right = new FormAttachment(100, -15);
        }
        lblMessage.setLayoutData(fd_lblMessage);
        lblMessage.setText(message);

        Button btnOk = new Button(shell, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });

        fd_lblMessage.bottom = new FormAttachment(btnOk, -10);
        FormData fd_btnOk = new FormData();
        fd_btnOk.bottom = new FormAttachment(100, -10);
        fd_btnOk.left = new FormAttachment(50, -35);
        fd_btnOk.right = new FormAttachment(50, 35);
        btnOk.setLayoutData(fd_btnOk);
        btnOk.setText(buttonOKText);
    }

    protected void open() {
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Shows a new dialog.
     * 
     * @param parent
     *            parent shell
     * @param type
     *            dialog type
     * @param title
     *            dialog title
     * @param message
     *            dialog message
     * @param modality
     *            modality type
     */
    public static void show(Shell parent, Type type, String title,
            String message, Modality modality) {

        final int mod;
        switch (modality) {
        case PRIMARY:
            mod = SWT.PRIMARY_MODAL;
            break;
        case APPLICATION:
            mod = SWT.APPLICATION_MODAL;
            break;
        case SYSTEM:
            mod = SWT.SYSTEM_MODAL;
            break;
        default:
            mod = SWT.NONE;
        }

        final MessageDialog dialog = new MessageDialog(parent,
                SWT.DIALOG_TRIM | mod, type, title, message);
        dialog.open();
    }
}
