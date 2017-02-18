package dad.calendario;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public  class MonthCalendar implements Initializable {

	private static final DateFormat FORMATTER = new SimpleDateFormat("MMMM");
	private static final String[] WEEK_DAYS = { "L", "M", "X", "J", "V", "S", "D" };

	// model
	private IntegerProperty month;
	private IntegerProperty year;
	private ObjectProperty<LocalDate> today;
	// view
	@FXML
	private Label monthNameLabel;
	@FXML
	private List<Label> weekDaysLabel;

	@FXML
	private List<Label> daysLabel;
	
	private Node view;

	private SimpleObjectProperty<LocalDate> diaPulsadoEvent;

	public MonthCalendar()  {
		diaPulsadoEvent = new SimpleObjectProperty<>(this,"diaPulsadoEvent",LocalDate.now());

		today = new SimpleObjectProperty<>(this, "today", LocalDate.now());
		today.addListener((obs, oldValue, newValue) -> onModelChanged());

	
		month = new SimpleIntegerProperty(this, "month", 0);
	
		year = new SimpleIntegerProperty(this, "year", 0);
		
	
	

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MonthCalendarView.fxml"));
			loader.setController(this);
			view=loader.load();
		} catch (IOException e) {
		throw new RuntimeException();
		}
		
		
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		int i = 0;
		for (Label label : weekDaysLabel) {
			label.setText(WEEK_DAYS[i++]);
		}

		i = 1;
		for (Label label : daysLabel) {
			label.setText("" + i++);
		}
		
		
		
	}
	
	   @FXML
	    void onDayLabelCliked(MouseEvent event) {
		   if (event.getButton() == MouseButton.PRIMARY){
			   System.out.println("Entrando en MothCalendar::onDayLabelClicked");
	    	Label labelCliked = (Label)event.getSource();
	    	try {
				diaPulsadoEvent.set(null);
	    		diaPulsadoEvent.set(LocalDate.of(getYear(), getMonth(), Integer.valueOf(labelCliked.getText())));
	    		
				
			} catch (NumberFormatException e) {
				
				System.err.println("error al parsear MothCalendar::onDayLabelClicked ");
			}
		   }
		   System.out.println("saliendo en MothCalendar::onDayLabelClicked");
	    }

	


	



	private void onModelChanged() {
	
		int first = DateUtils.firstDay(year.get(), month.get()) - 1;
		int last = DateUtils.lastDay(year.get(), month.get());
		for (int i = 0; i < first; i++) {
			daysLabel.get(i).setText("");
			daysLabel.get(i).getStyleClass().remove("today");
			daysLabel.get(i).getStyleClass().add("emptyday");
		}
		
		

		for (int i = first, j = 1; i < first + last; i++, j++) {
			daysLabel.get(i).setText("" + j);
			if (month.get() == today.getValue().getMonthValue() && j == today.get().getDayOfMonth()
					&& year.get() == today.getValue().getYear()) {
				daysLabel.get(i).getStyleClass().add("today");
			} else {
				daysLabel.get(i).getStyleClass().remove("today");
			}
		}

		for (int i = first + last; i < daysLabel.size(); i++) {
			daysLabel.get(i).setText("");
			daysLabel.get(i).getStyleClass().remove("today");
			daysLabel.get(i).getStyleClass().add("emptyday");

		}
		Date day = DateUtils.day(year.get(), month.get(), 1);
		monthNameLabel.setText(FORMATTER.format(day));
	}

	public void initDate(int month, int year ){
		
		this.month.set(month);
		this.year.set(year);
		
		this.month.addListener((observable, oldValue, newValue) -> {
			onModelChanged();
		});
		this.year.addListener((observable, oldValue, newValue) -> {
			onModelChanged();
		});
		
		onModelChanged();
	}

	public IntegerProperty monthProperty() {
		return this.month;
	}

	public int getMonth() {
		return this.monthProperty().get();
	}

	public void setMonth(final int month) {
		this.monthProperty().set(month);
	}

	public IntegerProperty yearProperty() {
		return this.year;
	}

	public int getYear() {
		return this.yearProperty().get();
	}

	public void setYear(final int year) {
		this.yearProperty().set(year);
	}

	public Node getView() {
		return view;
	}
	

	public List<Label> getDaysLabel() {
		return daysLabel;
	}

	public final ObjectProperty<LocalDate> todayProperty() {
		return this.today;
	}
	
	public final LocalDate getToday() {
		return this.todayProperty().get();
	}
	
	public final void setToday(final LocalDate today) {
		this.todayProperty().set(today);
	}
	
 



	public SimpleObjectProperty<LocalDate> diaPulsadoEventProperty() {
		return this.diaPulsadoEvent;
	}
	



	public LocalDate getDiaPulsadoEvent() {
		return this.diaPulsadoEventProperty().get();
	}
	




	//TODO BORRAR
	public Label getLabelByDayOfMoth(int dayParam){
		String dia = dayParam+"";	
		Label returnedLabel = null;
		for (Label dayL : daysLabel) {
			if (dayL.getText().equals(dia)) {
				returnedLabel=dayL;
			}
		}
		
		return returnedLabel;
	}



	

}
