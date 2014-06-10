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
 * Class for showing option (boolean) dialogs.
 * 
 * @author Paul Vorbach
 */
public class OptionDialog extends Dialog {
    /**
     * Text for this dialog's "Yes" button. Change this value before creating
     * dialogs if you want to localize your application.
     */
    public static String buttonYesText = "Yes";

    /**
     * Text for this dialog's "No" button. Change this value before creating
     * dialogs if you want to localize your application.
     */
    public static String buttonNoText = "No";

    private final Shell shell;
    private boolean result = true;

    /**
     * OptionDialog types.
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
        ERROR,

        QUESTION;
    }

    private OptionDialog(Shell parent, int style, Type type, String title,
            String question) {
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
        case QUESTION:
            icon = display.getSystemImage(SWT.ICON_QUESTION);
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
        lblMessage.setText(question);

        final Button btnYes = new Button(shell, SWT.NONE);

        fd_lblMessage.bottom = new FormAttachment(btnYes, -10);
        FormData fd_btnYes = new FormData();
        fd_btnYes.bottom = new FormAttachment(100, -10);
        fd_btnYes.left = new FormAttachment(50, -75);
        fd_btnYes.right = new FormAttachment(50, -5);
        btnYes.setLayoutData(fd_btnYes);
        btnYes.setText(buttonYesText);

        final Button btnNo = new Button(shell, SWT.NONE);

        FormData fd_btnNo = new FormData();
        fd_btnNo.bottom = new FormAttachment(100, -10);
        fd_btnNo.left = new FormAttachment(50, 5);
        fd_btnNo.right = new FormAttachment(50, 75);
        btnNo.setLayoutData(fd_btnNo);
        btnNo.setText(buttonNoText);

        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.widget == btnYes) {
                    result = true;
                } else if (e.widget == btnNo) {
                    result = false;
                }

                shell.dispose();
            }
        };

        btnYes.addSelectionListener(selectionAdapter);
        btnNo.addSelectionListener(selectionAdapter);
    }

    private boolean open() {
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
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
     * @param question
     *            dialog question
     * @param modality
     *            modality type
     * 
     * @return {@code true} if "Yes" has been clicked. {@code false} otherwise.
     */
    public static boolean show(Shell parent, Type type, String title,
            String question, Modality modality) {

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

        final OptionDialog dialog = new OptionDialog(parent,
                SWT.DIALOG_TRIM | mod, type, title, question);
        return dialog.open();
    }
}
