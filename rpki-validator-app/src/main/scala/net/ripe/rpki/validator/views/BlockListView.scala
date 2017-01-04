package net.ripe.rpki.validator.views

import net.ripe.rpki.validator.lib.Validation.FeedbackMessage
import net.ripe.rpki.validator.models.{BlockFilter, BlockList}

import scala.xml.Text

/**
  * Created by fimka on 14/10/16.
  */
class BlockListView(blockList: BlockList, validatedIanaBlockFilter: Set[BlockFilter], params: Map[String, String] = Map.
  empty, messages: Seq[FeedbackMessage] = Seq.empty) extends View with ViewHelpers {
  private val fieldNameToText = Map("prefix" -> "Prefix")
  //  val currentRtrPrefixes = getCurrentRtrPrefixes()

  def tab = Tabs.BlockListTab
  def title = Text("Blocklist")
  def body = {
    <div>{ renderMessages(messages, fieldNameToText) }</div>
      <div class="alert-message block-message info" data-alert="alert">
        <a class="close" href="#">×</a>
      </div>
      <div>
      </div>
      <h2>Add entry</h2>
      <div class="well">
        <form method="POST" class="form-stacked">
          <fieldset>
            <div>
              <div class="span4"><label for="block-prefix">Prefix</label></div>
              <div class="span12"></div>
            </div>
            <div class="span4">
              <input id="block-prefix" type="text" name="prefix" value={ params.getOrElse("prefix", "") }
                     placeholder="IPv4 or IPv6 prefix (required)"/>
              <input id="block-prefix" type="hidden" name="origin" value={ "Manual" }
              />
            </div>
            <div class="span2">
              <input type="submit" class="btn primary" value="Add"/>
            </div>
          </fieldset>
        </form>
      </div>
      <div>
        <h2>Current entries</h2>{
        <table id="blocklist-table" class="zebra-striped" style="display: none;">
          <thead>
            <tr>
              <th>Prefix</th><th>Origin</th><th>&nbsp;</th>
            </tr>
          </thead>
          <tbody>{
            val createTable: Set[BlockFilter] =  (blockList.entries) ++ validatedIanaBlockFilter
            for (entry <- createTable) yield {
              <tr>
                <td>{ entry.prefix }</td>
                <td>{entry.origin}</td>
                <td>
                  <form method="POST" action="/blockList" style="padding:0;margin:0;">
                    <input type="hidden" name="_method" value="DELETE"/>
                    <input type="hidden" name="prefix" value={ entry.prefix.toString }/>
                    <input type="hidden" name="origin" value={ entry.origin }/>
                    <input type="submit" class="btn" value="delete"/>
                  </form>
                </td>
              </tr>
            }
            } </tbody>
        </table>
          <script><!--
$(document).ready(function() {
  $('#blocklist-table').dataTable({
      "sPaginationType": "full_numbers",
      "aoColumns": [null,
        null,
        { "bSortable": false }
      ]
    }).show();
  $('[rel=popover]').popover({
    "live": true,
    "html": true,
    "placement": "below",
    "offset": 10
  }).live('click', function (e) {
    e.preventDefault();
  });
});
// --></script>

        }
      </div>
  }
}