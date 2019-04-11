Design document for the stock emulator application:

All the source code pertaining to this application are inside the stockemulation package.

This application follows a MVC architecture.

All the classes related to the model are in the model package under the stockemulation package.
All the classes related to the controller are in the controller package under the stockemulation package.
All the classes related to the view are in the view package under the stockemulation package.

We have a Util class to check input validity(based on business logic) before calling model's methods
for a response UI in a separate Util package.

View:
The view package has two different views one to support a CLI and one for a GUI.

The view from the command line interface remains the same without any changes it
takes in a PrintStream to print prompts and results to the user via the command line.

A new View(Interface implementation) was added to support the GUI. This view has
support for hooks to be added from the controller to handle callbacks for UI elements.

We also add a custom dialog to act as a form to support the buy stock operation.
We wanted a dialog robust enough to support form validation on each input,
support for custom layout and override the submit submit buttons to check form validity.

Notes on the view: we added support for reading from file and writing to file as menu options, this is easy to miss.

Controller:
We have two different controller interface implementations, one for the GUI and one for the CLI.

Controller - Modifications:
We repurposed the previous interface and implementation:
*The public facing changes we made in the interface was the parameter type of the model
Since we wrote an extended model which added onto the features of the previous version,
it will not break any of the existing implementations.
The impact for this change is minimal too as a controller object was created only once in the main method.

We also added support for new functionality in the implementation:
i.e adding options to the menu that is displayed to the user.
As we had already followed command pattern, these changes were minimal and isolated.
Since the changes were incremental and not something entirely new it did not make sense to re write them.

Controller - Additions to existing implementation:
We added new commands to support new features i.e read to file and write to file and buying with commission.
Code additions were minimal as all operations (like picking portfolio, getting date, stock info etc..)
were abstracted out in the previous version, they just had to be inherited.

We added a GUI controller that works with a GUI view.
Handling of callbacks from the view is abstracted out to a separate interface called Feature(This helps decouple our view and controller).

Both our controllers follow this principle:
The controller takes in an instance of the model and the view. Controller obtains data from the model, uses
it to perform business logic and sends the result to the user via the view.

Model:

Model - Modifications:
Some functions returned processed string instead of returning raw data to controller,
this was changed to return raw data as model should not worry about the format.

In order to support new features we extended the existing model Interface and Implementation.
This did not require any public facing modifications.
Since we had already decoupled our portfolio and stock classes, adding new features required extending them.

We flattened our model hierarchy and we expose only the model classes to the world, the other classes are package private.

Additions:
We also added a public constructor to allow the user to specify the datasource.(It takes an enum)
(The infrastructure to pick a datasource already existed but was not exposed as it was previously not required).

We extended the existing model to add 3 new operations:
*Read portfolios from files in JSON format.
*Write portfolios to files in JSON format.
*commission fees can be specified while buying stocks.

Get cost basis now will account for commission fees.

Note: Since our application already supported accessing data from Alphavantage APIs and caching it, we didn't have to add it in this assignment.
