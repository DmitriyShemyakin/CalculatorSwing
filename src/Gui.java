import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Gui implements ActionListener, KeyListener {


    private double result = 0;
    private String operator = "=";
    private boolean calculationFlag = true;

    private JTextField textField = new JTextField("0");//Поле вывода результата
    JPanel panel;
    JFrame frame;

    MyDrawPanel myDrawPanel;
    JButton button;


    public static void main(String[] args) {


        Gui gui = new Gui();
        gui.goGui();
    }

    public void  goGui(){
        myDrawPanel = new MyDrawPanel();
        frame = new JFrame();//Создаем экран (фрейм)
        panel = new JPanel();//Создаем панель в которой будут располагаться кнопки


        panel.setLayout(new GridLayout(4,4));//Задаем панели макет в виде таблицы(4 на 4)
        Font fontButtonReset = new Font("fontButton", Font.PLAIN, 20);// шрифт для кнопки сброс
        JButton buttonReset = new JButton("Сброс");
        buttonReset.setBackground(Color.pink);
        buttonReset.setFont(fontButtonReset);

        //Массив с названием кнопок
        String[] buttonName = new String[]{"7", "8", "9", "/", "4", "5", "6",
                "*", "1", "2", "3", "-", "0", ".", "=", "+"};

        //цикл создания кнопок(пробегаемся по массивку с названием кнопок)
        for (String tmp: buttonName) {

            button = new JButton(tmp);//Создаем кнопку и передаем название из массива
            Font fontButton = new Font("fontButton", Font.PLAIN, 30);// шрифт для кнопок
            button.setBackground(Color.PINK);
            button.setFont(fontButton);
            panel.add(button);//передаем кнопку в апнель
            button.addActionListener(this);//добовляем кнопке слушатель
        }

        //Создаем шрифт для fontTextField, в параметрах передаем имя шрифта, стиль и размер
        Font fontTextField = new Font("fontTextField", Font.BOLD, 70);
        textField.setEditable(false);//запрещаем возможность редактирования textField
        textField.setFont(fontTextField);



        //слушатель для кнопки сброс
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("0");
                result = 0;
                operator = "=";
                calculationFlag = true;
            }
        });

        frame.getContentPane().add(BorderLayout.NORTH, textField);//Добавляем textField в фрейм в позицию NORTH
        frame.getContentPane().add(panel);//Передаем во фрейм нашу панель с кнопками
        frame.getContentPane().add(BorderLayout.SOUTH, buttonReset);
        frame.addKeyListener(this);



        frame.setSize(400,500);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int red = (int) (Math.random() * 255);//преобразуем число с плавающей точкой в int
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        Color randomColor = new Color(red, green, blue);
        textField.setBackground(randomColor);
        play();


        String cmd = e.getActionCommand();//метод возвращает в виде строки string надпись на кнопке
        //проверяем нажата ли цифра 0 до 9 или "."
        if ('0' <= cmd.charAt(0) && cmd.charAt(0) <= '9' || cmd.equals(".")) {
            if (calculationFlag) {//если calculating = true
                textField.setText(cmd);//меняем текст в textField на нажатую кнопку
                calculationFlag = false;
            }else{
            textField.setText(textField.getText() + cmd);}


        } else { //если выбрано не число и не точка
//            (этот if сработает только если мы выбрали минус в самом начале)
            if (calculationFlag) { //проверяем calculating == true
                if (cmd.equals("-")) {// если выбран минус, то меня текст в textField на минус
                    textField.setText(cmd);
                    calculationFlag = false;
                } else // если выбран не минус то, оператор равен выбранному значению
                    operator = cmd;
            } else { //если выбрано не число и не точка, а calculating = false
                double x = Double.parseDouble(textField.getText());//считываем число с textField
                calculate(x); // вызываем метод
                operator = cmd;
                calculationFlag = true;
            }
        }

    }
    private void calculate(double x) {
        if (operator.equals("+")){
            result += x;}
        else if (operator.equals("-")){
            result -= x;}
        else if (operator.equals("*")){
            result *= x;}
        else if (operator.equals("/")){
            result /= x;}
        else if (operator.equals("=")){
            result = x;}
        textField.setText(String.valueOf(result));
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {



    }

    @Override
    public void keyReleased(KeyEvent e) {


    }


    public void play (){
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            Sequence seq = new Sequence(Sequence.PPQ,4);
            Track track = seq.createTrack();

            for(int i = 1; i<2; i += 1){
                track.add(makeEvent(144, 1, i,100, i));
            }
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(220);
            sequencer.start();

        }catch (Exception ex){ex.printStackTrace();}
    }


    public static MidiEvent makeEvent (int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a,tick);
        } catch (Exception e){}
        return event;
    }

}

class MyDrawPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        //random возвращает число от 0 до 1(не включительно)
        int red = (int) (Math.random() * 255);//преобразуем число с плавающей точкой в int
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        Color randomColor = new Color(red, green, blue);
        g.setColor(randomColor);
        g.fillOval(70,70,100,100);
    }
}




